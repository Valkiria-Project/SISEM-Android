package com.skgtecnologia.sisem.data.incident.remote.model

import com.skgtecnologia.sisem.domain.incident.model.IncidentModel
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
    patients = patients.map { it.mapToDomain() },
    resources = resources.map { it.mapToDomain() }
)
