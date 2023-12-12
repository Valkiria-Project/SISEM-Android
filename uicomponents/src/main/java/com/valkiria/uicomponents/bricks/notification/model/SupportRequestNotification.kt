package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import java.time.LocalDateTime

data class SupportRequestNotification(
    override val notificationType: NotificationType = SUPPORT_REQUEST_ON_THE_WAY,
    override val dateTime: LocalDateTime,
    val resourceTypeCode: String
) : NotificationData
