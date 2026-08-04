package com.skgtecnologia.sisem.ui.map

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.domain.incident.usecases.ObserveActiveIncident
import com.skgtecnologia.sisem.domain.notification.usecases.ObserveNotifications
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
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

class MapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var observeActiveIncident: ObserveActiveIncident

    @MockK
    private lateinit var observeNotifications: ObserveNotifications

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    /**
     * The state is shared WhileSubscribed, so nothing flows until something collects it.
     * Keeping a collector alive on the background scope is what makes the combine run.
     */
    private fun TestScope.subscribedViewModel(): MapViewModel {
        val viewModel = MapViewModel(observeActiveIncident, observeNotifications)
        backgroundScope.keepCollecting(viewModel)
        return viewModel
    }

    @Test
    fun `the state starts empty before either source emits`() = runTest(UnconfinedTestDispatcher()) {
        every { observeActiveIncident.invoke() } returns flowOf()
        every { observeNotifications.invoke() } returns flowOf()

        val viewModel = subscribedViewModel()

        Assert.assertEquals(MapUiState(), viewModel.uiState.value)
    }

    @Test
    fun `both sources are combined into a single state`() = runTest(UnconfinedTestDispatcher()) {
        val incident = mockk<IncidentUiModel>()
        val notifications = listOf(mockk<NotificationUiModel>())
        every { observeActiveIncident.invoke() } returns flowOf(incident)
        every { observeNotifications.invoke() } returns flowOf(notifications)

        val viewModel = subscribedViewModel()

        Assert.assertEquals(incident, viewModel.uiState.value.incident)
        Assert.assertEquals(notifications, viewModel.uiState.value.notifications)
    }

    @Test
    fun `clearing the active incident does not drop the notifications`() = runTest(UnconfinedTestDispatcher()) {
        val incident = mockk<IncidentUiModel>()
        val notifications = listOf(mockk<NotificationUiModel>())
        val incidents = MutableStateFlow<IncidentUiModel?>(incident)
        every { observeActiveIncident.invoke() } returns incidents
        every { observeNotifications.invoke() } returns flowOf(notifications)

        val viewModel = subscribedViewModel()
        Assert.assertEquals(incident, viewModel.uiState.value.incident)

        // The crew closes the incident: the map has to let go of it, but the
        // notifications it is showing are not part of that and must survive.
        incidents.value = null

        Assert.assertEquals(null, viewModel.uiState.value.incident)
        Assert.assertEquals(notifications, viewModel.uiState.value.notifications)
    }
}

private fun CoroutineScope.keepCollecting(viewModel: MapViewModel) {
    launch { viewModel.uiState.collect { } }
}
