package com.skgtecnologia.sisem.data.incident.remote.model

import com.valkiria.uicomponents.components.incident.model.IncidentUiDetailModel
import com.squareup.moshi.Json

data class IncidentDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String,
    @Json(name = "code_sisem") val codeSisem: String,
    @Json(name = "address") val address: String,
    @Json(name = "address_reference_point") val addressReferencePoint: String,
    @Json(name = "premier_one_date") val premierOneDate: String,
    @Json(name = "premier_one_hour") val premierOneHour: String,
    @Json(name = "incident_type") val incidentType: IncidentTypeResponse,
    @Json(name = "doctor_auth_name") val doctorAuthName: String
)

fun IncidentDetailResponse.mapToUi(): IncidentUiDetailModel = IncidentUiDetailModel(
    id = id,
    code = code,
    codeSisem = codeSisem,
    address = address,
    addressReferencePoint = addressReferencePoint,
    premierOneDate = premierOneDate,
    premierOneHour = premierOneHour,
    incidentType = incidentType.mapToUi(),
    doctorAuthName = doctorAuthName
)
