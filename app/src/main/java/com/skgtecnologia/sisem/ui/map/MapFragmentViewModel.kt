package com.skgtecnologia.sisem.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import com.skgtecnologia.sisem.ui.commons.extensions.locationFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    fusedLocationClient: FusedLocationProviderClient,
    observeActiveIncident: ObserveActiveIncident
) : ViewModel() {

    val uiState = combine(
        observeActiveIncident.invoke(),
        fusedLocationClient.locationFlow()
    ) { incident, location ->
        MapFragmentUiState(
            incident = incident,
            lastLocation = location
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(STATE_FLOW_STARTED_TIME),
        initialValue = MapFragmentUiState()
    )
}
