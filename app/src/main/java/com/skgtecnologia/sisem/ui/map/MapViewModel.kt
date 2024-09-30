package com.skgtecnologia.sisem.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.skgtecnologia.sisem.domain.notification.usecases.ObserveNotifications
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    observeActiveIncident: ObserveActiveIncident,
    observeNotifications: ObserveNotifications
) : ViewModel() {

    val uiState = combine(
        observeActiveIncident.invoke(),
        observeNotifications.invoke()
    ) { incident, notifications ->
        MapUiState(
            incident = incident,
            notifications = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(STATE_FLOW_STARTED_TIME),
        initialValue = MapUiState()
    )
}
