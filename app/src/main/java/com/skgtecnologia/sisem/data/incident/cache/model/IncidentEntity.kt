package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.notification.model.NotificationType
import com.valkiria.uicomponents.bricks.notification.model.getNotificationDataByType
import com.valkiria.uicomponents.bricks.notification.model.getNotificationRawDataByType

@Entity(
    tableName = "incident",
    indices = [Index(value = ["data"], unique = true)]
)
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @ColumnInfo(name = "notificationType") val notificationType: NotificationType,
    @ColumnInfo(name = "data") val data: Map<String, String>
)

fun IncidentEntity.mapToDomain(): NotificationData? = with(this) {
    getNotificationDataByType(this.data)
}
//
fun NotificationData.mapToCache(): IncidentEntity = with(this) {
    IncidentEntity(
        notificationType = this.notificationType,
        data = getNotificationRawDataByType(this)
    )
}
