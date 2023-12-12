package com.valkiria.uicomponents.components.incident.model

import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification

data class IncidentUiModel(
    val id: Long? = null,
    val incident: IncidentUiDetailModel,
    val patients: List<PatientUiModel>,
    val resources: List<ResourceUiModel>,
    val transmiRequests: List<TransmiNotification>? = null,
    val isActive: Boolean = true
)
