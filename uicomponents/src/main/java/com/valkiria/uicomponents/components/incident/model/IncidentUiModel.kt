package com.valkiria.uicomponents.components.incident.model

import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification

data class IncidentUiModel(
    val id: Long? = null,
    val incident: IncidentDetailUiModel,
    val patients: List<PatientUiModel>,
    val resources: List<ResourceUiModel>,
    val incidentPriority: IncidentPriority? = null,
    val transmiRequests: List<TransmiNotification>? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isActive: Boolean = true
)
