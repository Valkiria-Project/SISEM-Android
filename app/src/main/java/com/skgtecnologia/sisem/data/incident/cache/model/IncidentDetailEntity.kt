package com.skgtecnologia.sisem.data.incident.cache.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.valkiria.uicomponents.components.incident.model.IncidentUiDetailModel

@Entity(tableName = "incident_detail")
data class IncidentDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "codeSisem") val codeSisem: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "address_reference_point") val addressReferencePoint: String,
    @ColumnInfo(name = "premier_one_date") val premierOneDate: String,
    @ColumnInfo(name = "premier_one_hour") val premierOneHour: String,
    @Embedded(prefix = "incident_type_") val incidentType: IncidentTypeEntity,
    @ColumnInfo(name = "doctor_auth_name") val doctorAuthName: String
)

fun IncidentDetailEntity.mapToDomain(): IncidentUiDetailModel = with(this) {
    IncidentUiDetailModel(
        id = id,
        code = code,
        codeSisem = codeSisem,
        address = address,
        addressReferencePoint = addressReferencePoint,
        premierOneDate = premierOneDate,
        premierOneHour = premierOneHour,
        incidentType = incidentType.mapToDomain(),
        doctorAuthName = doctorAuthName
    )
}

fun IncidentUiDetailModel.mapToCache(): IncidentDetailEntity = with(this) {
    IncidentDetailEntity(
        id = id,
        code = code,
        codeSisem = codeSisem,
        address = address,
        addressReferencePoint = addressReferencePoint,
        premierOneDate = premierOneDate,
        premierOneHour = premierOneHour,
        incidentType = incidentType.mapToCache(),
        doctorAuthName = doctorAuthName
    )
}
