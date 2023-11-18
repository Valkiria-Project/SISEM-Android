package com.skgtecnologia.sisem.domain.notification.repository

import com.skgtecnologia.sisem.domain.notification.model.NotificationData

interface NotificationRepository {

    suspend fun storeNotification(notification: NotificationData)
}
