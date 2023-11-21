package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.skgtecnologia.sisem.domain.incident.model.IncidentTypeModel

@Entity(tableName = "incident_type")
data class IncidentTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "code") val code: String
)

fun IncidentTypeEntity.mapToDomain(): IncidentTypeModel = with(this) {
    IncidentTypeModel(
        id = id,
        code = code
    )
}

fun IncidentTypeModel.mapToCache(): IncidentTypeEntity = with(this) {
    IncidentTypeEntity(
        id = id,
        code = code
    )
}
