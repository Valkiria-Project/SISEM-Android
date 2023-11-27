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
import com.valkiria.uicomponents.bricks.notification.model.StretcherRetentionEnableNotification
import com.valkiria.uicomponents.bricks.notification.model.SupportRequestNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioAuthorizationNotification
import com.valkiria.uicomponents.bricks.notification.model.TransmilenioDeniedNotification
import java.util.UUID

data class NotificationUiModel(
    val identifier: String = UUID.randomUUID().toString(),
    val icon: String,
    val iconColor: String,
    val title: String,
    val description: String? = null,
    val contentLeft: String? = null,
    val contentRight: String? = null
)

fun NotificationData.mapToUi(): NotificationUiModel {
    return when (this) {
        is IncidentAssignedNotification -> NotificationUiModel(
            icon = INCIDENT_ASSIGNED.icon,
            iconColor = INCIDENT_ASSIGNED.iconColor,
            title = INCIDENT_ASSIGNED.title,
            description = this.cru,
            contentLeft = this.address,
            contentRight = this.hour
        )

        is TransmilenioAuthorizationNotification -> NotificationUiModel(
            icon = TRANSMILENIO_AUTHORIZATION.icon,
            iconColor = TRANSMILENIO_AUTHORIZATION.iconColor,
            title = TRANSMILENIO_AUTHORIZATION.title,
            description = TRANSMILENIO_AUTHORIZATION.descriptionDecorator
                .plus(this.authorizationNumber),
            contentLeft = TRANSMILENIO_AUTHORIZATION.contentLeftDecorator.plus(this.authorizes)
        )

        is TransmilenioDeniedNotification -> NotificationUiModel(
            icon = TRANSMILENIO_DENIED.icon,
            iconColor = TRANSMILENIO_DENIED.iconColor,
            title = TRANSMILENIO_DENIED.title
        )

        is NoPreOperationalGeneratedCrueNotification -> NotificationUiModel(
            icon = NO_PRE_OPERATIONAL_GENERATED_CRUE.icon,
            iconColor = NO_PRE_OPERATIONAL_GENERATED_CRUE.iconColor,
            title = NO_PRE_OPERATIONAL_GENERATED_CRUE.title
        )

        is SupportRequestNotification -> NotificationUiModel(
            icon = SUPPORT_REQUEST_ON_THE_WAY.icon,
            iconColor = SUPPORT_REQUEST_ON_THE_WAY.iconColor,
            title = SUPPORT_REQUEST_ON_THE_WAY.title,
            description = this.resourceTypeCode
        )

        is IpsPatientTransferredNotification -> NotificationUiModel(
            icon = IPS_PATIENT_TRANSFERRED.icon,
            iconColor = IPS_PATIENT_TRANSFERRED.iconColor,
            title = IPS_PATIENT_TRANSFERRED.title,
            description = this.headquartersName,
            contentLeft = this.headquartersAddress
        )

        is StretcherRetentionEnableNotification -> NotificationUiModel(
            icon = STRETCHER_RETENTION_ENABLE.icon,
            iconColor = STRETCHER_RETENTION_ENABLE.iconColor,
            title = STRETCHER_RETENTION_ENABLE.title
        )

        is ClosingAPHNotification -> NotificationUiModel(
            icon = CLOSING_OF_APH.icon,
            iconColor = CLOSING_OF_APH.iconColor,
            title = CLOSING_OF_APH.title.plus(this.consecutiveNumber),
            description = this.updateTimeObservationsAttachments
        )

        is TransmiNotification -> error("Invalid Transmi Notification Type")
    }
}
