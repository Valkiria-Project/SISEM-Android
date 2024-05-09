package com.skgtecnologia.sisem.data.notification.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.notification.cache.dao.NotificationDao
import com.skgtecnologia.sisem.data.notification.cache.model.mapToCache
import com.skgtecnologia.sisem.data.notification.cache.model.mapToDomain
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.bricks.notification.mapToUi
import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationCacheDataSource @Inject constructor(
    private val notificationDao: NotificationDao
) {

    suspend fun storeNotification(notification: NotificationData) =
        notificationDao.insertNotification(notification.mapToCache())

    @CheckResult
    fun getActiveIncidentNotification(): Flow<IncidentAssignedNotification>? =
        notificationDao.observeNotifications()
            ?.map { notifications ->
                notifications?.map {
                    it.mapToDomain()
                }
            }
            ?.filterIsInstance(IncidentAssignedNotification::class)
            ?.catch { throwable ->
                error("error observing the notifications ${throwable.localizedMessage}")
            }
            ?.flowOn(Dispatchers.IO)

    @CheckResult
    fun observeNotifications(): Flow<List<NotificationUiModel>?>? =
        notificationDao.observeNotifications()
            ?.map { notifications ->
                notifications?.map { it.mapToDomain()?.mapToUi() ?: error("Map error") }
            }
            ?.catch { throwable ->
                error("error observing the notifications ${throwable.localizedMessage}")
            }
            ?.flowOn(Dispatchers.IO)
}
