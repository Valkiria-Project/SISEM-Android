package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
import java.time.LocalDateTime
import java.time.LocalTime

data class TransmilenioDeniedNotification(
    override val notificationType: NotificationType = TRANSMILENIO_DENIED,
    override val time: LocalTime,
    override val dateTime: LocalDateTime,
    val authorizationNumber: String
) : TransmiNotification
