package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

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
                idIncident = notification.incidentNumber,
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

    override fun observeNotifications(): Flow<List<NotificationUiModel>?> =
        notificationCacheDataSource.observeNotifications()
}
