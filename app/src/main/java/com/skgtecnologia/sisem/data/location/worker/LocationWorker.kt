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

        val vehicleCode = operationCacheDataSource
            .observeOperationConfig()
            .firstOrNull()
            ?.vehicleCode
            .orEmpty()

        if (vehicleCode.isBlank()) {
            return ListenableWorker.Result.failure()
        }

        return updateLocation.invoke(lat, lon).fold(
            onSuccess = { ListenableWorker.Result.success() },
            onFailure = { ListenableWorker.Result.retry() }
        )
    }

    companion object {
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
    }
}
