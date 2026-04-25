package com.valkiria.uicomponents.bricks.notification.model

import java.time.LocalDateTime
import java.time.LocalTime

data class UpdateVehicleStatusNotification(
    override val notificationType: NotificationType = NotificationType.UPDATE_VEHICLE_STATUS,
    override val time: LocalTime,
    override val dateTime: LocalDateTime,
) : NotificationData
