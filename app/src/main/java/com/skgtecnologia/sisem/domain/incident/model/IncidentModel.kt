package com.skgtecnologia.sisem.domain.incident.model

data class IncidentModel(
    val incident: IncidentDetailModel,
    val patientModels: List<PatientModel>,
    val resources: List<ResourceModel>
)

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

data class IncidentTypeModel(
    val id: Int,
    val code: String
)

data class PatientModel(
    val id: Int,
    val fullName: String,
    val idAph: Int
)

data class ResourceModel(
    val id: Int,
    val resourceId: Int,
    val resource: ResourceDetailModel
)

data class ResourceDetailModel(
    val id: Int,
    val code: String,
    val transitAgency: String,
    val icTransitAgency: String
)
