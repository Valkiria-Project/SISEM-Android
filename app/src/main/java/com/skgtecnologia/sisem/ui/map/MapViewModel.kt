package com.skgtecnologia.sisem.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.skgtecnologia.sisem.domain.notification.usecases.ObserveNotifications
import com.skgtecnologia.sisem.ui.commons.extensions.locationFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    fusedLocationClient: FusedLocationProviderClient,
    observeActiveIncident: ObserveActiveIncident,
    observeNotifications: ObserveNotifications
) : ViewModel() {

    val uiState = combine(
        observeActiveIncident.invoke(),
        fusedLocationClient.locationFlow(),
        observeNotifications.invoke()
    )
    { incident, location, notifications ->
        MapUiState(
            incident = incident,
            lastLocation = location,
            notifications = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = MapUiState()
    )
}
