package com.skgtecnologia.sisem.domain.incident.model

data class IncidentDetailModel(
    val id: Int,
    val code: String,
    val codeSisem: String,
    val address: String,
    val addressReferencePoint: String,
    val premierOneDate: String,
    val premierOneHour: String,
    val incidentType: IncidentTypeModel,
    val doctorAuthName: String
)
