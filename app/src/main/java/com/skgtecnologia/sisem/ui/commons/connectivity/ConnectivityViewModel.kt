package com.skgtecnologia.sisem.ui.commons.connectivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import com.skgtecnologia.sisem.data.offline.outbox.OutboxStore
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    outboxStore: OutboxStore
) : ViewModel() {

    val uiState: StateFlow<ConnectivityUiState> = combine(
        networkMonitor.connectivity,
        outboxStore.observePendingCount(),
        outboxStore.observeRejectedCount()
    ) { connectivity, pending, rejected ->
        ConnectivityUiState(
            // Only a missing network counts as offline here. A network Android could not validate
            // may still reach SISEM — see Connectivity.UNVALIDATED — and a permanent "offline"
            // banner on ambulances whose SIMs never validate would teach crews to ignore it.
            isOffline = connectivity == Connectivity.OFFLINE,
            pendingCount = pending,
            rejectedCount = rejected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STARTED_TIME),
        initialValue = ConnectivityUiState(
            isOffline = networkMonitor.connectivity.value == Connectivity.OFFLINE
        )
    )
}
