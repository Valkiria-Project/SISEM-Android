package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.toIncidentNumber
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class NotificationRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val incidentCacheDataSource: IncidentCacheDataSource,
    private val incidentRemoteDataSource: IncidentRemoteDataSource,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : NotificationRepository {

    override suspend fun storeNotification(notification: NotificationData) {
        notificationCacheDataSource.storeNotification(notification)

        if (notification is IncidentAssignedNotification) {
            incidentRemoteDataSource.getIncidentInfo(
                idIncident = notification.incidentNumber.toIncidentNumber(),
                idTurn = authCacheDataSource.observeAccessToken()
                    .first()
                    ?.turn
                    ?.id
                    ?.toString()
                    .orEmpty(),
                codeVehicle = operationCacheDataSource.observeOperationConfig()
                    .first()
                    ?.vehicleCode
                    .orEmpty()
            ).onSuccess {
                incidentCacheDataSource.storeIncident(it)
            }
        }
    }
}
