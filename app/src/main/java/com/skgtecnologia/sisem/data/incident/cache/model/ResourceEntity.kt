package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.ResourceUiModel

@Entity(tableName = "resource")
data class ResourceEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "resource_id") val resourceId: Int,
    @Embedded(prefix = "resource_") val resource: ResourceDetailEntity
)

fun ResourceEntity.mapToUi(): ResourceUiModel = with(this) {
    ResourceUiModel(
        id = id,
        resourceId = resourceId,
        resource = resource.mapToUi()
    )
}

fun ResourceUiModel.mapToCache(): ResourceEntity = with(this) {
    ResourceEntity(
        id = id,
        resourceId = resourceId,
        resource = resource.mapToCache()
    )
}
