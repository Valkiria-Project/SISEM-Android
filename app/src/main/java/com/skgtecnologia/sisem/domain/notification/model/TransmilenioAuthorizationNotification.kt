package com.skgtecnologia.sisem.domain.notification.model

import com.skgtecnologia.sisem.domain.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION

data class TransmilenioAuthorizationNotification(
    override val notificationType: NotificationType = TRANSMILENIO_AUTHORIZATION,
    val authorizationNumber: String,
    val authorizes: String
) : NotificationData
