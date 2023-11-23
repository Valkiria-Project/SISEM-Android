package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION

data class TransmilenioAuthorizationNotification(
    override val notificationType: NotificationType = TRANSMILENIO_AUTHORIZATION,
    val authorizationNumber: String,
    val authorizes: String
) : NotificationData
