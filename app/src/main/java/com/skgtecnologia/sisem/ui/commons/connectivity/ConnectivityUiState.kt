package com.skgtecnologia.sisem.ui.commons.connectivity

import com.valkiria.uicomponents.bricks.connectivity.isConnectivityBannerVisible

data class ConnectivityUiState(
    val isOffline: Boolean = false,
    val pendingCount: Int = 0,
    val rejectedCount: Int = 0
) {
    val isBannerVisible: Boolean
        get() = isConnectivityBannerVisible(isOffline, pendingCount, rejectedCount)
}
