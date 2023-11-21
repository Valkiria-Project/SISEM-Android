package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.incident.model.ResourceModel

@Entity(tableName = "resource")
data class ResourceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "resource_id") val resourceId: Int,
    @Embedded(prefix = "resource_") val resource: ResourceDetailEntity
)

fun ResourceEntity.mapToDomain(): ResourceModel = with(this) {
    ResourceModel(
        id = id,
        resourceId = resourceId,
        resource = resource.mapToDomain()
    )
}

fun ResourceModel.mapToCache(): ResourceEntity = with(this) {
    ResourceEntity(
        id = id,
        resourceId = resourceId,
        resource = resource.mapToCache()
    )
}
