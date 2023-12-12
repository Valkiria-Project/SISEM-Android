package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION
import java.time.LocalDateTime

data class TransmilenioAuthorizationNotification(
    override val notificationType: NotificationType = TRANSMILENIO_AUTHORIZATION,
    override val dateTime: LocalDateTime,
    val authorizationNumber: String,
    val authorizes: String,
    val journey: String
) : TransmiNotification
