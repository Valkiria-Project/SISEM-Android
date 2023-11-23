package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.NO_PRE_OPERATIONAL_GENERATED_CRUE

data class NoPreOperationalGeneratedCrueNotification(
    override val notificationType: NotificationType = NO_PRE_OPERATIONAL_GENERATED_CRUE
) : NotificationData
