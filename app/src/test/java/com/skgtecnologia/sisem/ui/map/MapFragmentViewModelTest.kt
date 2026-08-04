package com.skgtecnologia.sisem.ui.map

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapFragmentViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var observeActiveIncident: ObserveActiveIncident

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    /**
     * Shared WhileSubscribed, so the upstream only runs while something collects. Without
     * a live collector every assertion would read the initial value and pass for the
     * wrong reason.
     */
    private fun TestScope.subscribedViewModel(): MapFragmentViewModel {
        val viewModel = MapFragmentViewModel(observeActiveIncident)
        backgroundScope.keepCollecting(viewModel)
        return viewModel
    }

    @Test
    fun `the state starts without an incident`() = runTest(UnconfinedTestDispatcher()) {
        every { observeActiveIncident.invoke() } returns flowOf()

        val viewModel = subscribedViewModel()

        Assert.assertEquals(MapFragmentUiState(), viewModel.uiState.value)
    }

    @Test
    fun `the active incident is exposed`() = runTest(UnconfinedTestDispatcher()) {
        val incident = mockk<IncidentUiModel>()
        every { observeActiveIncident.invoke() } returns flowOf(incident)

        val viewModel = subscribedViewModel()

        Assert.assertEquals(incident, viewModel.uiState.value.incident)
    }

    @Test
    fun `closing the incident empties the state`() = runTest(UnconfinedTestDispatcher()) {
        val incident = mockk<IncidentUiModel>()
        val incidents = MutableStateFlow<IncidentUiModel?>(incident)
        every { observeActiveIncident.invoke() } returns incidents

        val viewModel = subscribedViewModel()
        Assert.assertEquals(incident, viewModel.uiState.value.incident)

        incidents.value = null

        Assert.assertEquals(null, viewModel.uiState.value.incident)
    }
}

private fun CoroutineScope.keepCollecting(viewModel: MapFragmentViewModel) {
    launch { viewModel.uiState.collect { } }
}
