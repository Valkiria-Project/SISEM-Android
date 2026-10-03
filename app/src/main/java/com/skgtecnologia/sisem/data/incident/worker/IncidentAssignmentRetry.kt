package com.skgtecnologia.sisem.data.incident.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.skgtecnologia.sisem.data.incident.AssignedIncidentFetcher
import com.skgtecnologia.sisem.data.incident.IncidentAssignment
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val INCIDENT_ASSIGNMENT_WORK = "incident-assignment"
private const val BACKOFF_SECONDS = 15L
private const val KEY_INCIDENT_NUMBER = "incident_number"
private const val KEY_INCIDENT_PRIORITY = "incident_priority"
private const val KEY_GEOLOCATION = "geolocation"
private const val KEY_ASSIGNED_AT = "assigned_at"

/**
 * An incident is still worth fetching this long after it was assigned. Past that the crew has
 * long been told by radio, and storing it now could put an incident that is already over on the
 * map as the active one.
 */
internal const val MAX_ASSIGNMENT_AGE_MILLIS = 2 * 60 * 60 * 1000L

fun interface IncidentAssignmentRetryScheduler {
    fun schedule(assignment: IncidentAssignment)
}

@Singleton
class WorkManagerIncidentAssignmentRetryScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : IncidentAssignmentRetryScheduler {

    override fun schedule(assignment: IncidentAssignment) {
        // Replaced, not appended: only the latest assignment is the vehicle's current incident.
        WorkManager.getInstance(context).enqueueUniqueWork(
            INCIDENT_ASSIGNMENT_WORK,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<IncidentAssignmentRetryWorker>()
                .setInputData(assignment.toData(System.currentTimeMillis()))
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_SECONDS, TimeUnit.SECONDS)
                .build()
        )
    }
}

internal fun IncidentAssignment.toData(assignedAt: Long): Data = workDataOf(
    KEY_INCIDENT_NUMBER to incidentNumber,
    KEY_INCIDENT_PRIORITY to incidentPriority,
    KEY_GEOLOCATION to geolocation,
    KEY_ASSIGNED_AT to assignedAt
)

internal fun Data.toAssignment(): IncidentAssignment? {
    val incidentNumber = getString(KEY_INCIDENT_NUMBER) ?: return null
    return IncidentAssignment(
        incidentNumber = incidentNumber,
        incidentPriority = getString(KEY_INCIDENT_PRIORITY).orEmpty(),
        geolocation = getString(KEY_GEOLOCATION).orEmpty()
    )
}

/**
 * Fetches an assigned incident whose details could not be loaded when the push arrived.
 *
 * The push itself reaches the device on very little signal, but the incident's details — the
 * patients and their medical histories — come from a second call, and without them the crew
 * cannot open a history for that incident at all.
 */
@HiltWorker
class IncidentAssignmentRetryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val fetcher: AssignedIncidentFetcher
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = retryAssignment(inputData, fetcher, System.currentTimeMillis())
}

internal suspend fun retryAssignment(
    input: Data,
    fetcher: AssignedIncidentFetcher,
    now: Long
): ListenableWorker.Result {
    val assignment = input.toAssignment()
    val age = now - input.getLong(KEY_ASSIGNED_AT, now)

    return when {
        assignment == null -> {
            ListenableWorker.Result.failure()
        }

        age > MAX_ASSIGNMENT_AGE_MILLIS -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(age)
            Timber.w("Giving up on incident ${assignment.incidentNumber}: assigned $minutes min ago")
            ListenableWorker.Result.failure()
        }

        else -> {
            fetcher.fetch(assignment).fold(
            onSuccess = { ListenableWorker.Result.success() },
            onFailure = {
                Timber.d("Incident ${assignment.incidentNumber} still unavailable: ${it.message}")
                ListenableWorker.Result.retry()
            }
        )
        }
    }
}
