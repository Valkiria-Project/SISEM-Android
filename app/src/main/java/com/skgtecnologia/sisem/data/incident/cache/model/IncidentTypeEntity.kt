package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.IncidentTypeUiModel

@Entity(tableName = "incident_type")
data class IncidentTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "code") val code: String
)

fun IncidentTypeEntity.mapToUi(): IncidentTypeUiModel = with(this) {
    IncidentTypeUiModel(
        id = id,
        code = code
    )
}

fun IncidentTypeUiModel.mapToCache(): IncidentTypeEntity = with(this) {
    IncidentTypeEntity(
        id = id,
        code = code
    )
}
