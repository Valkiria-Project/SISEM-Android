package com.skgtecnologia.sisem.data.location.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.location.usecases.UpdateLocation
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import java.time.Instant

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val updateLocation: UpdateLocation,
    val operationCacheDataSource: OperationCacheDataSource
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): ListenableWorker.Result {
        val lat = inputData.getDouble(KEY_LATITUDE, 0.0)
        val lon = inputData.getDouble(KEY_LONGITUDE, 0.0)
        val capturedAtMillis = inputData.getLong(KEY_CAPTURED_AT, 0L)

        // Drop stale samples that piled up while offline so the backend
        // does not record ancient positions as if they were current.
        if (capturedAtMillis > 0L) {
            val ageMillis = System.currentTimeMillis() - capturedAtMillis
            if (ageMillis > MAX_STALENESS_MILLIS) {
                Timber.tag("Location").d(
                    "Dropping stale sample ageMs=$ageMillis (lat=$lat, lon=$lon)"
                )
                return ListenableWorker.Result.success()
            }
        }

        val vehicleCode = operationCacheDataSource
            .observeOperationConfig()
            .firstOrNull()
            ?.vehicleCode
            .orEmpty()

        // Retry instead of failing so the worker is preserved across
        // re-authentication. Failing would drop the queued sample.
        if (vehicleCode.isBlank()) {
            Timber.tag("Location").d("vehicleCode blank, retrying later")
            return ListenableWorker.Result.retry()
        }

        val capturedAt = if (capturedAtMillis > 0L) {
            Instant.ofEpochMilli(capturedAtMillis)
        } else {
            Instant.now()
        }

        return updateLocation.invoke(lat, lon, capturedAt).fold(
            onSuccess = { ListenableWorker.Result.success() },
            onFailure = { ListenableWorker.Result.retry() }
        )
    }

    companion object {
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
        const val KEY_CAPTURED_AT = "captured_at_millis"

        // Drop samples older than 5 minutes when finally dequeued.
        private const val MAX_STALENESS_MILLIS = 5 * 60 * 1000L
    }
}
