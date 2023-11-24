package com.valkiria.uicomponents.components.incident.model

data class IncidentUiDetailModel(
    val id: Int,
    val code: String,
    val codeSisem: String,
    val address: String,
    val addressReferencePoint: String,
    val premierOneDate: String,
    val premierOneHour: String,
    val incidentType: IncidentUiTypeModel,
    val doctorAuthName: String
)
