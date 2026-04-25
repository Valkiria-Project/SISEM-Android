package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.ResourceDetailUiModel

@Entity(tableName = "resource_detail")
data class ResourceDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "ic_transit_agency") val icTransitAgency: String
)

fun ResourceDetailEntity.mapToUi(): ResourceDetailUiModel = with(this) {
    ResourceDetailUiModel(
        id = id,
        code = code,
        name = name,
        icTransitAgency = icTransitAgency
    )
}

fun ResourceDetailUiModel.mapToCache(): ResourceDetailEntity = with(this) {
    ResourceDetailEntity(
        id = id,
        code = code,
        name = name,
        icTransitAgency = icTransitAgency
    )
}
