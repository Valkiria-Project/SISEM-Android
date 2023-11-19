package com.skgtecnologia.sisem.domain.notification.repository

import com.valkiria.uicomponents.bricks.notification.NotificationData

interface NotificationRepository {

    suspend fun storeNotification(notification: NotificationData)
}
