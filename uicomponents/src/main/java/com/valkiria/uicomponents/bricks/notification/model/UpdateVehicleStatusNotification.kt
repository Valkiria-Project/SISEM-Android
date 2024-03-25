package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.CLOSING_OF_APH
import java.time.LocalTime

data class UpdateVehicleStatusNotification(
    override val notificationType: NotificationType = CLOSING_OF_APH,
    override val time: LocalTime
) : NotificationData
