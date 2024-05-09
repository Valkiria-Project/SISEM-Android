package com.skgtecnologia.sisem.domain.notification.repository

import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun storeNotification(notification: NotificationData)

    fun observeNotifications(): Flow<List<NotificationUiModel>?>?
}
