package com.skgtecnologia.sisem.ui.map

import com.mapbox.navigation.tripdata.progress.model.TripProgressUpdateValue
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel

data class MapFragmentUiState(
    val incident: IncidentUiModel? = null,
    val tripProgress: TripProgressUpdateValue? = null
)
