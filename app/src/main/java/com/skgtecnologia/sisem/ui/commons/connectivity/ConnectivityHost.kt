package com.skgtecnologia.sisem.ui.commons.connectivity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valkiria.uicomponents.bricks.connectivity.ConnectivityBanner

/**
 * Puts the connectivity banner above every screen. While it shows, the screens below are told
 * the status bar is already taken care of, so the few that pad for it do not leave a gap under
 * the banner.
 */
@Composable
fun ConnectivityHost(
    viewModel: ConnectivityViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        ConnectivityBanner(
            isOffline = uiState.isOffline,
            pendingCount = uiState.pendingCount,
            rejectedCount = uiState.rejectedCount
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .then(
                    if (uiState.isBannerVisible) {
                        Modifier.consumeWindowInsets(WindowInsets.statusBars)
                    } else {
                        Modifier
                    }
                )
        ) {
            content()
        }
    }
}
