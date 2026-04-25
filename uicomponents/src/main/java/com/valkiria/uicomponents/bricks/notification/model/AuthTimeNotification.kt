package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.AUTH_TIME
import java.time.LocalDateTime
import java.time.LocalTime

data class AuthTimeNotification(
    override val notificationType: NotificationType = AUTH_TIME,
    override val time: LocalTime,
    override val dateTime: LocalDateTime
) : NotificationData
