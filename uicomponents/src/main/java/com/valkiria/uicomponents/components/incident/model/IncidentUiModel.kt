package com.valkiria.uicomponents.components.incident.model

data class IncidentUiModel(
    val incident: IncidentUiDetailModel,
    val patients: List<PatientUiModel>,
    val resources: List<ResourceUiModel>,
    val isActive: Boolean = true
)
