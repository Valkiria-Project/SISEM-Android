package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
import java.time.LocalTime

data class TransmilenioDeniedNotification(
    override val notificationType: NotificationType = TRANSMILENIO_DENIED,
    override val time: LocalTime,
    val authorizationNumber: String
) : TransmiNotification
