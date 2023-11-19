package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED

data class TransmilenioDeniedNotification(
    override val notificationType: NotificationType = TRANSMILENIO_DENIED
) : NotificationData
