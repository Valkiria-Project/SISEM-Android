package com.skgtecnologia.sisem.data.location

import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.data.location.remote.LocationRemoteDataSource
import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.location.LocationRepository
import com.valkiria.uicomponents.utlis.TimeUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.time.Instant
import javax.inject.Inject

private const val MOBILE_APP_ORIGIN = 2

class LocationRepositoryImpl @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val locationRemoteDataSource: LocationRemoteDataSource,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : LocationRepository {

    override suspend fun updateLocation(latitude: Double, longitude: Double) {
        val currentIncidentNotification = notificationCacheDataSource
            .getActiveIncidentNotification()
            ?.first()

        locationRemoteDataSource.sendUpdatedLocation(
            idVehicle = operationCacheDataSource
                .observeOperationConfig()
                .firstOrNull()
                ?.vehicleCode.orEmpty(),
            idDevice = androidIdProvider.getAndroidId(),
            idOrigin = MOBILE_APP_ORIGIN,
            latitude = latitude,
            longitude = longitude,
            originAt = TimeUtils.getLocalDateTime(Instant.now()).toString(),
            idIncident = currentIncidentNotification?.incidentNumber
        )
    }
}
