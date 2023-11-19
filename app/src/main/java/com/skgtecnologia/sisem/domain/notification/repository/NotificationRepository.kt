package com.skgtecnologia.sisem.domain.notification.repository

import com.valkiria.uicomponents.bricks.notification.model.NotificationData

interface NotificationRepository {

    suspend fun storeNotification(notification: NotificationData)
}
