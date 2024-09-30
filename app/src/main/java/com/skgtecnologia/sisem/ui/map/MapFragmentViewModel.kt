package com.skgtecnologia.sisem.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    observeActiveIncident: ObserveActiveIncident
) : ViewModel() {

    val uiState = observeActiveIncident.invoke()
        .filterNotNull()
        .map { incident ->
            MapFragmentUiState(
                incident = incident,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(STATE_FLOW_STARTED_TIME),
            initialValue = MapFragmentUiState()
        )
}
