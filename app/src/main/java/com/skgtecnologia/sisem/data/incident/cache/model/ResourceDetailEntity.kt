package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.ResourceUiDetailModel

@Entity(tableName = "resource_detail")
data class ResourceDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "code") val code: String,
    @Embedded(prefix = "transit_agency") val transitAgency: String,
    @Embedded(prefix = "ic_transit_agency") val icTransitAgency: String
)

fun ResourceDetailEntity.mapToDomain(): ResourceUiDetailModel = with(this) {
    ResourceUiDetailModel(
        id = id,
        code = code,
        transitAgency = transitAgency,
        icTransitAgency = icTransitAgency
    )
}

fun ResourceUiDetailModel.mapToCache(): ResourceDetailEntity = with(this) {
    ResourceDetailEntity(
        id = id,
        code = code,
        transitAgency = transitAgency,
        icTransitAgency = icTransitAgency
    )
}
