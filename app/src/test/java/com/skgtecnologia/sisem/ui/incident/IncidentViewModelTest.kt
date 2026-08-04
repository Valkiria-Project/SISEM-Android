package com.skgtecnologia.sisem.ui.incident

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.incident.usecases.GetIncidentScreen
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val APH_IDENTIFIER = "APH-123"

class IncidentViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getIncidentScreen: GetIncidentScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    private lateinit var incidentViewModel: IncidentViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    private suspend fun givenScreenLoads() {
        coEvery { getIncidentScreen.invoke() } returns Result.success(emptyScreenModel)
    }

    @Test
    fun `when the screen loads it is exposed and the loader is cleared`() = runTest {
        givenScreenLoads()

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)

        Assert.assertEquals(emptyScreenModel, incidentViewModel.uiState.screenModel)
        Assert.assertEquals(false, incidentViewModel.uiState.isLoading)
        Assert.assertEquals(null, incidentViewModel.uiState.infoEvent)
    }

    @Test
    fun `when the screen fails to load the error is surfaced`() = runTest {
        coEvery { getIncidentScreen.invoke() } returns Result.failure(Throwable())

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)

        Assert.assertEquals(SERVER_ERROR_TITLE, incidentViewModel.uiState.infoEvent?.title)
        Assert.assertEquals(false, incidentViewModel.uiState.isLoading)
        Assert.assertEquals(null, incidentViewModel.uiState.screenModel)
    }

    @Test
    fun `when goBack is called the navigation asks to go back`() = runTest {
        givenScreenLoads()

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.goBack()

        Assert.assertEquals(true, incidentViewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when navigating to stretcher retention the identifier travels along`() = runTest {
        givenScreenLoads()

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.navigateToStretcherRetention(APH_IDENTIFIER)

        Assert.assertEquals(
            APH_IDENTIFIER,
            incidentViewModel.uiState.navigationModel?.stretcherRetentionAph
        )
    }

    @Test
    fun `when navigating to the medical history the patient travels along`() = runTest {
        givenScreenLoads()

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.navigateToAphView(APH_IDENTIFIER)

        Assert.assertEquals(
            APH_IDENTIFIER,
            incidentViewModel.uiState.navigationModel?.patientAph
        )
    }

    @Test
    fun `when the navigation event is consumed the destination is cleared`() = runTest {
        givenScreenLoads()

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.goBack()
        incidentViewModel.consumeNavigationEvent()

        Assert.assertEquals(null, incidentViewModel.uiState.navigationModel)
        Assert.assertEquals(false, incidentViewModel.uiState.isLoading)
    }

    @Test
    fun `the re-auth banner logs the user out and announces it`() = runTest {
        givenScreenLoads()
        coEvery { logoutCurrentUser.invoke() } returns Result.success(USERNAME)

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        coVerify { logoutCurrentUser.invoke() }
        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(USERNAME) }
    }

    @Test
    fun `a failed logout still announces it so the user is not stranded`() = runTest {
        givenScreenLoads()
        coEvery { logoutCurrentUser.invoke() } returns Result.failure(Throwable("boom"))

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `any other action only dismisses the banner`() = runTest {
        coEvery { getIncidentScreen.invoke() } returns Result.failure(Throwable())

        incidentViewModel = IncidentViewModel(getIncidentScreen, logoutCurrentUser)
        incidentViewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, incidentViewModel.uiState.infoEvent)
        coVerify(exactly = 0) { logoutCurrentUser.invoke() }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
