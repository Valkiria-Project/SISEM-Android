package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.CLOSING_OF_APH
import java.time.LocalDateTime

data class ClosingAPHNotification(
    override val notificationType: NotificationType = CLOSING_OF_APH,
    override val dateTime: LocalDateTime,
    val consecutiveNumber: String,
    val updateTimeObservationsAttachments: String
) : NotificationData
