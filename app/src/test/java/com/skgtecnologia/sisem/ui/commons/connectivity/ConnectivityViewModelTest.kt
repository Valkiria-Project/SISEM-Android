package com.skgtecnologia.sisem.ui.commons.connectivity

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.connectivity.Connectivity
import com.skgtecnologia.sisem.commons.connectivity.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ConnectivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val connectivity = MutableStateFlow(Connectivity.ONLINE)
    private val networkMonitor = object : NetworkMonitor {
        override val connectivity: StateFlow<Connectivity> = this@ConnectivityViewModelTest.connectivity
    }

    @Test
    fun `with signal there is nothing to show`() = runTest(UnconfinedTestDispatcher()) {
        val viewModel = ConnectivityViewModel(networkMonitor)
        backgroundScope.launch { viewModel.uiState.collect {} }

        assertEquals(false, viewModel.uiState.value.isBannerVisible)
    }

    @Test
    fun `losing the network shows the banner and getting it back hides it`() =
        runTest(UnconfinedTestDispatcher()) {
            val viewModel = ConnectivityViewModel(networkMonitor)
            backgroundScope.launch { viewModel.uiState.collect {} }

            connectivity.value = Connectivity.OFFLINE
            assertEquals(true, viewModel.uiState.value.isOffline)
            assertEquals(true, viewModel.uiState.value.isBannerVisible)

            connectivity.value = Connectivity.ONLINE
            assertEquals(false, viewModel.uiState.value.isBannerVisible)
        }

    @Test
    fun `a network that never validates does not leave the banner up for good`() =
        runTest(UnconfinedTestDispatcher()) {
            val viewModel = ConnectivityViewModel(networkMonitor)
            backgroundScope.launch { viewModel.uiState.collect {} }

            connectivity.value = Connectivity.UNVALIDATED

            assertEquals(false, viewModel.uiState.value.isOffline)
        }

    @Test
    fun `the state starts from where the network already is`() {
        connectivity.value = Connectivity.OFFLINE

        val viewModel = ConnectivityViewModel(networkMonitor)

        assertEquals(true, viewModel.uiState.value.isOffline)
    }
}
