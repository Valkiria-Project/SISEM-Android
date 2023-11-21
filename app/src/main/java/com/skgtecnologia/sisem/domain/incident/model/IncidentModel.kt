package com.skgtecnologia.sisem.domain.incident.model

data class IncidentModel(
    val incident: IncidentDetailModel,
    val patients: List<PatientModel>,
    val resources: List<ResourceModel>
)
