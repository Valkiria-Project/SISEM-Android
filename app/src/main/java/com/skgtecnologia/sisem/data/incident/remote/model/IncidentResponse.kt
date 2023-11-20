package com.skgtecnologia.sisem.data.incident.remote.model

import com.skgtecnologia.sisem.domain.incident.model.IncidentDetailModel
import com.skgtecnologia.sisem.domain.incident.model.IncidentModel
import com.skgtecnologia.sisem.domain.incident.model.IncidentTypeModel
import com.skgtecnologia.sisem.domain.incident.model.PatientModel
import com.skgtecnologia.sisem.domain.incident.model.ResourceDetailModel
import com.skgtecnologia.sisem.domain.incident.model.ResourceModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidentResponse(
    @Json(name = "incident") val incident: IncidentDetailResponse,
    @Json(name = "patients") val patients: List<PatientResponse>,
    @Json(name = "resources") val resources: List<ResourceResponse>
)

fun IncidentResponse.mapToDomain(): IncidentModel = IncidentModel(
    incident = incident.mapToDomain(),
    patientModels = patients.map { it.mapToDomain() },
    resources = resources.map { it.mapToDomain() }
)

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

fun IncidentDetailResponse.mapToDomain(): IncidentDetailModel = IncidentDetailModel(
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

data class IncidentTypeResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String
)

fun IncidentTypeResponse.mapToDomain(): IncidentTypeModel = IncidentTypeModel(
    id = id,
    code = code
)

data class PatientResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "id_aph") val idAph: Int
)

fun PatientResponse.mapToDomain(): PatientModel = PatientModel(
    id = id,
    fullName = fullName,
    idAph = idAph
)

data class ResourceResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "resource_id") val resourceId: Int,
    @Json(name = "resource") val resource: ResourceDetailResponse
)

fun ResourceResponse.mapToDomain(): ResourceModel = ResourceModel(
    id = id,
    resourceId = resourceId,
    resource = resource.mapToDomain()
)

data class ResourceDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String,
    @Json(name = "transit_agency") val transitAgency: String,
    @Json(name = "ic_transit_agency") val icTransitAgency: String
)

fun ResourceDetailResponse.mapToDomain(): ResourceDetailModel = ResourceDetailModel(
    id = id,
    code = code,
    transitAgency = transitAgency,
    icTransitAgency = icTransitAgency
)
