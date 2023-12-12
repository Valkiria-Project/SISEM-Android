package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import java.time.LocalTime

data class SupportRequestNotification(
    override val notificationType: NotificationType = SUPPORT_REQUEST_ON_THE_WAY,
    override val time: LocalTime,
    val resourceTypeCode: String
) : NotificationData
