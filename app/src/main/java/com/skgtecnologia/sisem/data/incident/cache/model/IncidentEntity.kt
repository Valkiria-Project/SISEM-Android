package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
@Entity(
    tableName = "incident",
    indices = [Index(value = ["incident_id"], unique = true)]
)
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,
    @Embedded(prefix = "incident_") val incident: IncidentDetailEntity,
    @ColumnInfo(name = "patients") val patients: List<PatientEntity>,
    @ColumnInfo(name = "resources") val resources: List<ResourceEntity>,
    @ColumnInfo(name = "is_active") val isActive: Boolean
)

fun IncidentEntity.mapToDomain(): IncidentUiModel = with(this) {
    IncidentUiModel(
        incident = incident.mapToDomain(),
        patients = patients.map { it.mapToDomain() },
        resources = resources.map { it.mapToDomain() },
        isActive = isActive
    )
}

fun IncidentUiModel.mapToCache(): IncidentEntity = with(this) {
    IncidentEntity(
        incident = incident.mapToCache(),
        patients = patients.map { it.mapToCache() },
        resources = resources.map { it.mapToCache() },
        isActive = isActive
    )
}
