package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.CLOSING_OF_APH

data class ClosingAPHNotification(
    override val notificationType: NotificationType = CLOSING_OF_APH,
    val consecutiveNumber: String,
    val updateTimeObservationsAttachments: String
) : NotificationData
