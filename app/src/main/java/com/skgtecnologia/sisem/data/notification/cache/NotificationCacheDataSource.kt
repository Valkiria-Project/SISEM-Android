package com.skgtecnologia.sisem.data.notification.cache

import com.skgtecnologia.sisem.data.notification.cache.dao.NotificationDao
import com.skgtecnologia.sisem.data.notification.cache.model.mapToCache
import com.valkiria.uicomponents.bricks.notification.NotificationData
import javax.inject.Inject

class NotificationCacheDataSource @Inject constructor(
    private val notificationDao: NotificationDao
) {

    suspend fun storeNotification(notification: NotificationData) =
        notificationDao.insertNotification(notification.mapToCache())
}
