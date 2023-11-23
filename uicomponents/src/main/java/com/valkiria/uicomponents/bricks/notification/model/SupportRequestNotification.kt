package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY

data class SupportRequestNotification(
    override val notificationType: NotificationType = SUPPORT_REQUEST_ON_THE_WAY,
    val resourceTypeCode: String
) : NotificationData
