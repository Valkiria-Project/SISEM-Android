package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.STRETCHER_RETENTION_ENABLE

data class StretcherRetentionEnableNotification(
    override val notificationType: NotificationType = STRETCHER_RETENTION_ENABLE
) : NotificationData
