package com.skgtecnologia.sisem.data.notification.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skgtecnologia.sisem.data.notification.cache.model.NotificationEntity
import com.valkiria.uicomponents.bricks.notification.model.NotificationType
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notificationEntity: NotificationEntity): Long

    @Query("SELECT * FROM notification order by dateTime desc")
    fun observeNotifications(): Flow<List<NotificationEntity>?>?

    @Query(
        """
            SELECT * FROM notification
            WHERE notificationType = :notificationType
            order by dateTime
            desc LIMIT 1
             """
    )
    suspend fun getActiveIncidentNotification(
        notificationType: NotificationType = NotificationType.INCIDENT_ASSIGNED
    ): NotificationEntity?
}
