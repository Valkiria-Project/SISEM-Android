package com.skgtecnologia.sisem.data.incident.remote.model

import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IncidentResponse(
    @Json(name = "incident") val incident: IncidentDetailResponse,
    @Json(name = "patients") val patients: List<PatientResponse>,
    @Json(name = "resources") val resources: List<ResourceResponse>
)

fun IncidentResponse.mapToUi(): IncidentUiModel = IncidentUiModel(
    incident = incident.mapToUi(),
    patients = patients.map { it.mapToUi() },
    resources = resources.map { it.mapToUi() }
)
