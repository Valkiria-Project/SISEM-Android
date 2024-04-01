package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import java.time.LocalDateTime
import java.time.LocalTime

data class SupportRequestNotification(
    override val notificationType: NotificationType = SUPPORT_REQUEST_ON_THE_WAY,
    override val time: LocalTime,
    override val dateTime: LocalDateTime,
    val resourceTypeCode: String
) : NotificationData
