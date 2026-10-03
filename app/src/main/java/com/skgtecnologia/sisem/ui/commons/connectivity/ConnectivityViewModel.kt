package com.skgtecnologia.sisem.ui.commons.connectivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val uiState: StateFlow<ConnectivityUiState> = networkMonitor.connectivity
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_STARTED_TIME),
            initialValue = networkMonitor.connectivity.value.toUiState()
        )

    // Only a missing network counts as offline here. A network Android could not validate may
    // still reach SISEM — see Connectivity.UNVALIDATED — and a permanent "offline" banner on
    // ambulances whose SIMs never validate would teach crews to ignore it.
    private fun Connectivity.toUiState() = ConnectivityUiState(isOffline = this == Connectivity.OFFLINE)
}
