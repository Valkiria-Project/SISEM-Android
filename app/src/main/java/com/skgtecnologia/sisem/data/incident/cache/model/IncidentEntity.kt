package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.bricks.notification.model.getTransmiNotificationDataByType
import com.valkiria.uicomponents.bricks.notification.model.getTransmiNotificationRawDataByType
import com.valkiria.uicomponents.components.incident.model.IncidentPriority
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
    @ColumnInfo(name = "incident_priority") val incidentPriority: IncidentPriority?,
    @ColumnInfo(name = "transmi_requests") val transmiRequests: List<Map<String, String>>?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "is_active") val isActive: Boolean
)

fun IncidentEntity.mapToUi(): IncidentUiModel = with(this) {
    IncidentUiModel(
        id = id,
        incident = incident.mapToUi(),
        patients = patients.map { it.mapToUi() },
        resources = resources.map { it.mapToUi() },
        transmiRequests = transmiRequests?.map {
            getTransmiNotificationDataByType(it)
        },
        incidentPriority = incidentPriority,
        latitude = latitude,
        longitude = longitude,
        isActive = isActive
    )
}

fun IncidentUiModel.mapToCache(): IncidentEntity = with(this) {
    IncidentEntity(
        incident = incident.mapToCache(),
        patients = patients.map { it.mapToCache() },
        resources = resources.map { it.mapToCache() },
        transmiRequests = transmiRequests?.map { getTransmiNotificationRawDataByType(it) },
        incidentPriority = incidentPriority,
        latitude = latitude,
        longitude = longitude,
        isActive = isActive
    )
}
