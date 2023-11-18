package com.skgtecnologia.sisem.data.notification

import com.skgtecnologia.sisem.data.notification.cache.NotificationCacheDataSource
import com.skgtecnologia.sisem.domain.notification.model.NotificationData
import com.skgtecnologia.sisem.domain.notification.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationCacheDataSource: NotificationCacheDataSource
) : NotificationRepository {

    override suspend fun storeNotification(notification: NotificationData) {
        notificationCacheDataSource.storeNotification(notification)
    }
}
