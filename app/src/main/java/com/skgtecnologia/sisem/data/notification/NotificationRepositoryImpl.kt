package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.commons.communication.IncidentEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import com.valkiria.uicomponents.bricks.notification.model.IpsPatientTransferredNotification
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification
import com.valkiria.uicomponents.bricks.notification.model.UpdateVehicleStatusNotification
import com.valkiria.uicomponents.components.incident.model.IncidentPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@Suppress("LongParameterList")
class NotificationRepositoryImpl @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val authCacheDataSource: AuthCacheDataSource,
    private val incidentCacheDataSource: IncidentCacheDataSource,
    private val incidentRemoteDataSource: IncidentRemoteDataSource,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource,
    private val operationRemoteDataSource: OperationRemoteDataSource
) : NotificationRepository {

    override suspend fun storeNotification(notification: NotificationData) {
        notificationCacheDataSource.storeNotification(notification)

        when (notification) {
            is IncidentAssignedNotification -> handleIncidentAssignedNotification(notification)
            is IpsPatientTransferredNotification ->
                handleIpsPatientTransferredNotification(notification)

            is TransmiNotification -> handleTransmiNotification(notification)
            is UpdateVehicleStatusNotification -> handleUpdateVehicleStatusNotification()
            else -> Timber.d("no-op")
        }
    }

    private suspend fun handleIncidentAssignedNotification(
        notification: IncidentAssignedNotification
    ) {
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
            val (longitude, latitude) = notification.geolocation.split(",")
            val incident = it.copy(
                incidentPriority = IncidentPriority.getPriority(notification.incidentPriority),
                latitude = latitude.toDoubleOrNull(),
                longitude = longitude.toDoubleOrNull()
            )
            incidentCacheDataSource.storeIncident(incident)
        }.onFailure {
            IncidentEventHandler.publishIncidentErrorEvent(it.mapToUi())
        }
    }

    private suspend fun handleIpsPatientTransferredNotification(
        notification: IpsPatientTransferredNotification
    ) {
        val (longitude, latitude) = checkNotNull(notification.geolocation?.split(","))
        val incident = checkNotNull(
            incidentCacheDataSource.observeActiveIncident().first()?.copy(
                latitude = latitude.toDoubleOrNull(),
                longitude = longitude.toDoubleOrNull()
            )
        )

        incidentCacheDataSource.storeIncident(incident)
    }

    private suspend fun handleTransmiNotification(notification: TransmiNotification) {
        val incident = checkNotNull(incidentCacheDataSource.observeActiveIncident().first())

        if (incident.id != null) {
            val transmiRequests = buildList {
                incident.transmiRequests?.let { addAll(it) }
                add(notification)
            }

            incidentCacheDataSource.updateTransmiStatus(incident.id!!, transmiRequests)
        }
    }

    private suspend fun handleUpdateVehicleStatusNotification() {
        val turnId = authCacheDataSource.observeAccessToken()
            .first()?.turn?.id?.toString()

        operationRemoteDataSource.getOperationConfig(androidIdProvider.getAndroidId(), turnId)
            .onSuccess {
                operationCacheDataSource.storeOperationConfig(it)
            }
    }

    override fun observeNotifications(): Flow<List<NotificationUiModel>> =
        notificationCacheDataSource.observeNotifications()
}
