package com.skgtecnologia.sisem.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.navigation.tripdata.progress.model.TripProgressUpdateValue
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    observeActiveIncident: ObserveActiveIncident
) : ViewModel() {

    private val tripProgress = MutableStateFlow<TripProgressUpdateValue?>(null)

    val uiState = combine(
        observeActiveIncident.invoke(),
        tripProgress
    ) { incident, progress ->
        MapFragmentUiState(
            incident = incident,
            tripProgress = progress
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(STATE_FLOW_STARTED_TIME),
        initialValue = MapFragmentUiState()
    )

    fun updateTripProgress(progress: TripProgressUpdateValue?) {
        tripProgress.value = progress
    }
}
