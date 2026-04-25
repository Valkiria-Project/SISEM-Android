package com.skgtecnologia.sisem.ui.map

import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel

data class MapUiState(
    val incident: IncidentUiModel? = null,
    val notifications: List<NotificationUiModel>? = null
)
