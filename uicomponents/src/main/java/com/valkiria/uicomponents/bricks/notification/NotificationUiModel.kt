package com.valkiria.uicomponents.bricks.notification

import com.valkiria.uicomponents.bricks.notification.model.ClosingAPHNotification
import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import com.valkiria.uicomponents.bricks.notification.model.IpsPatientTransferredNotification
import com.valkiria.uicomponents.bricks.notification.model.NoPreOperationalGeneratedCrueNotification
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.CLOSING_OF_APH
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.IPS_PATIENT_TRANSFERRED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.NO_PRE_OPERATIONAL_GENERATED_CRUE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.STRETCHER_RETENTION_ENABLE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.UPDATE_VEHICLE_STATUS
import com.valkiria.uicomponents.bricks.notification.model.StretcherRetentionEnableNotification
import com.valkiria.uicomponents.bricks.notification.model.SupportRequestNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioAuthorizationNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioDeniedNotification
import com.valkiria.uicomponents.bricks.notification.model.UpdateVehicleStatusNotification
import com.valkiria.uicomponents.components.incident.model.IncidentPriority
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalTimeAsString
import java.util.UUID

data class NotificationUiModel(
    val identifier: String = UUID.randomUUID().toString(),
    val icon: String,
    val iconColor: String,
    val title: String,
    val description: String? = null,
    val content: String? = null,
    val date: String? = null,
    val time: String? = null,
    val timeStamp: String
)

@Suppress("LongMethod")
fun NotificationData.mapToUi(): NotificationUiModel {
    val timeStamp = getLocalTimeAsString(time)

    return when (this) {
        is IncidentAssignedNotification -> NotificationUiModel(
            icon = INCIDENT_ASSIGNED.icon,
            iconColor = IncidentPriority.getPriority(incidentPriority).stringColor,
            title = INCIDENT_ASSIGNED.title,
            description = this.cru,
            content = this.address,
            date = this.incidentDate,
            time = this.hour,
            timeStamp = timeStamp
        )

        is TransmilenioAuthorizationNotification -> NotificationUiModel(
            icon = TRANSMILENIO_AUTHORIZATION.icon,
            iconColor = TRANSMILENIO_AUTHORIZATION.iconColor,
            title = TRANSMILENIO_AUTHORIZATION.title,
            description = TRANSMILENIO_AUTHORIZATION.descriptionDecorator
                .plus(this.authorizationNumber),
            content = TRANSMILENIO_AUTHORIZATION.contentLeftDecorator.plus(this.authorizes),
            timeStamp = timeStamp
        )

        is TransmilenioDeniedNotification -> NotificationUiModel(
            icon = TRANSMILENIO_DENIED.icon,
            iconColor = TRANSMILENIO_DENIED.iconColor,
            title = TRANSMILENIO_DENIED.title,
            timeStamp = timeStamp
        )

        is NoPreOperationalGeneratedCrueNotification -> NotificationUiModel(
            icon = NO_PRE_OPERATIONAL_GENERATED_CRUE.icon,
            iconColor = NO_PRE_OPERATIONAL_GENERATED_CRUE.iconColor,
            title = NO_PRE_OPERATIONAL_GENERATED_CRUE.title,
            timeStamp = timeStamp
        )

        is SupportRequestNotification -> NotificationUiModel(
            icon = SUPPORT_REQUEST_ON_THE_WAY.icon,
            iconColor = SUPPORT_REQUEST_ON_THE_WAY.iconColor,
            title = SUPPORT_REQUEST_ON_THE_WAY.title,
            description = this.resourceTypeCode,
            timeStamp = timeStamp
        )

        is IpsPatientTransferredNotification -> NotificationUiModel(
            icon = IPS_PATIENT_TRANSFERRED.icon,
            iconColor = IPS_PATIENT_TRANSFERRED.iconColor,
            title = IPS_PATIENT_TRANSFERRED.title,
            description = this.headquartersName,
            content = this.headquartersAddress,
            timeStamp = timeStamp
        )

        is StretcherRetentionEnableNotification -> NotificationUiModel(
            icon = STRETCHER_RETENTION_ENABLE.icon,
            iconColor = STRETCHER_RETENTION_ENABLE.iconColor,
            title = STRETCHER_RETENTION_ENABLE.title,
            timeStamp = timeStamp
        )

        is ClosingAPHNotification -> NotificationUiModel(
            icon = CLOSING_OF_APH.icon,
            iconColor = CLOSING_OF_APH.iconColor,
            title = CLOSING_OF_APH.title.plus(this.consecutiveNumber),
            description = this.updateTimeObservationsAttachments,
            timeStamp = timeStamp
        )

        is UpdateVehicleStatusNotification -> NotificationUiModel(
            icon = UPDATE_VEHICLE_STATUS.icon,
            iconColor = UPDATE_VEHICLE_STATUS.iconColor,
            title = UPDATE_VEHICLE_STATUS.title,
            timeStamp = timeStamp
        )

        is TransmiNotification -> error("Invalid Transmi Notification Type")
    }
}
