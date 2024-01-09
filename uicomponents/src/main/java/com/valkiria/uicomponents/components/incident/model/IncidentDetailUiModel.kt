package com.valkiria.uicomponents.components.incident.model

data class IncidentDetailUiModel(
    val id: Int,
    val code: String,
    val codeSisem: String,
    val address: String,
    val addressReferencePoint: String,
    val premierOneDate: String,
    val premierOneHour: String,
    val incidentType: IncidentTypeUiModel,
    val doctorAuthName: String
)
