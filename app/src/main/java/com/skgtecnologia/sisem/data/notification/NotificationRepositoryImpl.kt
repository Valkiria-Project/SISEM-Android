package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.commons.communication.IncidentEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.AssignedIncidentFetcher
import com.skgtecnologia.sisem.data.incident.IncidentAssignment
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.worker.IncidentAssignmentRetryScheduler
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

@Suppress("LongParameterList")
class NotificationRepositoryImpl @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val authCacheDataSource: AuthCacheDataSource,
    private val incidentCacheDataSource: IncidentCacheDataSource,
    private val assignedIncidentFetcher: AssignedIncidentFetcher,
    private val incidentAssignmentRetryScheduler: IncidentAssignmentRetryScheduler,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource,
    private val operationRemoteDataSource: OperationRemoteDataSource
) : NotificationRepository {

    override suspend fun storeNotification(notification: NotificationData) {
        notificationCacheDataSource.storeNotification(notification)

        when (notification) {
            is IncidentAssignedNotification -> {
                handleIncidentAssignedNotification(notification)
            }

            is IpsPatientTransferredNotification -> {
                handleIpsPatientTransferredNotification(notification)
            }

            is TransmiNotification -> {
                handleTransmiNotification(notification)
            }

            is UpdateVehicleStatusNotification -> {
                handleUpdateVehicleStatusNotification()
            }

            else -> {
                Timber.d("no-op")
            }
        }
    }

    private suspend fun handleIncidentAssignedNotification(
        notification: IncidentAssignedNotification
    ) {
        val assignment = IncidentAssignment(
            incidentNumber = notification.incidentNumber,
            incidentPriority = notification.incidentPriority,
            geolocation = notification.geolocation
        )

        assignedIncidentFetcher.fetch(assignment).onFailure {
            IncidentEventHandler.publishIncidentErrorEvent(it.mapToUi())
            // The push made it through on a weak signal, the details did not. Keep trying in the
            // background, or the crew could not open a medical history for this incident at all.
            incidentAssignmentRetryScheduler.schedule(assignment)
        }
    }

    private suspend fun handleIpsPatientTransferredNotification(
        notification: IpsPatientTransferredNotification
    ) {
        val geolocation = notification.geolocation
            ?: return Timber.w("IPS notification missing geolocation, skipping").let { }
        val (longitude, latitude) = geolocation.split(",")

        val incident = incidentCacheDataSource.observeActiveIncident().first()
            ?: return Timber.w("No active incident for IPS notification, skipping").let { }

        incidentCacheDataSource.storeIncident(
            incident.copy(
                latitude = latitude.toDoubleOrNull(),
                longitude = longitude.toDoubleOrNull()
            )
        )
    }

    private suspend fun handleTransmiNotification(notification: TransmiNotification) {
        val incident = incidentCacheDataSource.observeActiveIncident().first()
            ?: return Timber.w("No active incident for Transmi notification, skipping").let { }

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
            .onSuccess { operationModel ->
                operationCacheDataSource.storeOperationConfig(operationModel)

                incidentCacheDataSource.observeActiveIncident().first()?.let { activeIncident ->
                    incidentCacheDataSource.storeIncident(
                        activeIncident.copy(
                            isActive = operationModel.status
                        )
                    )
                }
            }
    }

    override fun observeNotifications(): Flow<List<NotificationUiModel>> =
        notificationCacheDataSource.observeNotifications()
}
