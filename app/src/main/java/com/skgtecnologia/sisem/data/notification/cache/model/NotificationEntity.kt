package com.skgtecnologia.sisem.data.notification.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.bricks.notification.NotificationData
import com.valkiria.uicomponents.bricks.notification.NotificationType
import com.valkiria.uicomponents.bricks.notification.getNotificationDataByType
import com.valkiria.uicomponents.bricks.notification.getNotificationRawDataByType

@Entity(
    tableName = "notification",
    indices = [Index(value = ["data"], unique = true)]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "notificationType") val notificationType: NotificationType,
    @ColumnInfo(name = "data") val data: Map<String, String>
)

fun NotificationEntity.mapToDomain(): NotificationData? = with(this) {
    getNotificationDataByType(this.data)
}

fun NotificationData.mapToCache(): NotificationEntity = with(this) {
    NotificationEntity(
        notificationType = this.notificationType,
        data = getNotificationRawDataByType(this)
    )
}
