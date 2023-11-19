package com.valkiria.uicomponents.bricks.notification

import com.valkiria.uicomponents.bricks.notification.NotificationType.TRANSMILENIO_AUTHORIZATION
import com.valkiria.uicomponents.bricks.notification.NotificationData
import com.valkiria.uicomponents.bricks.notification.NotificationType

data class TransmilenioAuthorizationNotification(
    override val notificationType: NotificationType = TRANSMILENIO_AUTHORIZATION,
    val authorizationNumber: String,
    val authorizes: String
) : NotificationData
