package com.skgtecnologia.sisem.data.notification.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.NotificationType
import com.valkiria.uicomponents.bricks.notification.model.getNotificationDataByType
import com.valkiria.uicomponents.bricks.notification.model.getNotificationRawDataByType
import java.time.LocalTime

@Entity(
    tableName = "notification",
    indices = [Index(value = ["data"], unique = true)]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "notificationType") val notificationType: NotificationType,
    @ColumnInfo(name = "time") val time: LocalTime,
    @ColumnInfo(name = "data") val data: Map<String, String>
)

fun NotificationEntity.mapToDomain(): NotificationData? = with(this) {
    getNotificationDataByType(this.data, this.time)
}

fun NotificationData.mapToCache(): NotificationEntity = with(this) {
    NotificationEntity(
        notificationType = this.notificationType,
        time = time,
        data = getNotificationRawDataByType(this)
    )
}
