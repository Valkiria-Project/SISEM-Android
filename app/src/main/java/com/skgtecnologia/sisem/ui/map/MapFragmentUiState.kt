package com.skgtecnologia.sisem.ui.map

import android.location.Location
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel

data class MapFragmentUiState(
    val lastLocation: Location? = null,
    val incident: IncidentUiModel? = null,
)
