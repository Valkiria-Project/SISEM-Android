package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.STRETCHER_RETENTION_ENABLE
import java.time.LocalDateTime
import java.time.LocalTime

data class StretcherRetentionEnableNotification(
    override val notificationType: NotificationType = STRETCHER_RETENTION_ENABLE,
    override val time: LocalTime,
    override val dateTime: LocalDateTime,
) : NotificationData
