package com.skgtecnologia.sisem.data.offline.outbox

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val OUTBOX_SYNC_WORK = "outbox-sync"
private const val BACKOFF_SECONDS = 30L

fun interface OutboxSyncScheduler {
    fun schedule()
}

@Singleton
class WorkManagerOutboxSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : OutboxSyncScheduler {

    override fun schedule() {
        // Appended rather than kept: a write queued while a run is finishing — after its last
        // look at the queue — would otherwise wait for the next trigger. An extra run that finds
        // the queue empty costs nothing.
        WorkManager.getInstance(context).enqueueUniqueWork(
            OUTBOX_SYNC_WORK,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            OneTimeWorkRequestBuilder<OutboxSyncWorker>()
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_SECONDS, TimeUnit.SECONDS)
                .build()
        )
    }
}
