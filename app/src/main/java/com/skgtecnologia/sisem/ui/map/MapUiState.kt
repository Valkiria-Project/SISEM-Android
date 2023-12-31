package com.skgtecnologia.sisem.ui.map

import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel

// Bogota default location expressed in terms of longitude and latitude
const val BOGOTA_DEFAULT_LNG = -74.1063086
const val BOGOTA_DEFAULT_LAT = 4.7593809

data class MapUiState(
    val location: Pair<Double, Double> = BOGOTA_DEFAULT_LNG to BOGOTA_DEFAULT_LAT,
    val incident: IncidentUiModel? = null,
    val notifications: List<NotificationUiModel>? = null
)
