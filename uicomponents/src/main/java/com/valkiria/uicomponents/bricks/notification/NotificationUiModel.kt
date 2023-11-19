package com.valkiria.uicomponents.bricks.notification

import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import com.valkiria.uicomponents.bricks.notification.model.NoPreOperationalGeneratedCrueNotification
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.NO_PRE_OPERATIONAL_GENERATED_CRUE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
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
            description = this.incidentNumber,
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
    }
}
