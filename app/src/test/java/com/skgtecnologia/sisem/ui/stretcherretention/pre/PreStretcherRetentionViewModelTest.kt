package com.skgtecnologia.sisem.ui.stretcherretention.pre

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.stretcherretention.errors.StretchRetentionErrors
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.GetPreStretcherRetentionScreen
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

private const val PATIENT = "APH-4471"

class PreStretcherRetentionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getPreStretcherRetentionScreen: GetPreStretcherRetentionScreen

    private lateinit var viewModel: PreStretcherRetentionViewModel

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

    private fun createViewModel() = PreStretcherRetentionViewModel(
        logoutCurrentUser,
        getPreStretcherRetentionScreen
    )

    private suspend fun givenScreenLoads(): PreStretcherRetentionViewModel {
        coEvery { getPreStretcherRetentionScreen.invoke() } returns Result.success(
            emptyScreenModel
        )
        return createViewModel()
    }

    @Test
    fun `when the screen loads it is exposed and the loader is cleared`() = runTest {
        viewModel = givenScreenLoads()

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `having no incident is not an error but an empty screen`() = runTest {
        coEvery { getPreStretcherRetentionScreen.invoke() } returns Result.failure(
            StretchRetentionErrors.NoIncidentId
        )

        viewModel = createViewModel()

        // A crew with no incident open has nothing to retain a stretcher for. That is a
        // normal state, so it gets its own screen instead of an error banner.
        Assert.assertEquals(null, viewModel.uiState.infoEvent)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
        Assert.assertNotEquals(null, viewModel.uiState.screenModel)
        Assert.assertEquals(1, viewModel.uiState.screenModel?.body?.size)
    }

    @Test
    fun `any other failure is surfaced as an error`() = runTest {
        coEvery { getPreStretcherRetentionScreen.invoke() } returns Result.failure(Throwable())

        viewModel = createViewModel()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
        Assert.assertEquals(null, viewModel.uiState.screenModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `navigating to the stretcher view carries the patient`() = runTest {
        viewModel = givenScreenLoads()

        viewModel.navigateToStretcherView(PATIENT)

        Assert.assertEquals(PATIENT, viewModel.uiState.navigationModel?.patientAph)
    }

    @Test
    fun `navigating back asks to go back`() = runTest {
        viewModel = givenScreenLoads()

        viewModel.navigateBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when the navigation event is consumed the destination is cleared`() = runTest {
        viewModel = givenScreenLoads()
        viewModel.navigateBack()

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `the re-auth banner logs the user out and announces it`() = runTest {
        viewModel = givenScreenLoads()
        coEvery { logoutCurrentUser.invoke() } returns Result.success(USERNAME)

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        coVerify { logoutCurrentUser.invoke() }
        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(USERNAME) }
    }

    @Test
    fun `a failed logout still announces it so the user is not stranded`() = runTest {
        viewModel = givenScreenLoads()
        coEvery { logoutCurrentUser.invoke() } returns Result.failure(Throwable("boom"))

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `any other action only dismisses the banner`() = runTest {
        coEvery { getPreStretcherRetentionScreen.invoke() } returns Result.failure(Throwable())
        viewModel = createViewModel()

        viewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
        coVerify(exactly = 0) { logoutCurrentUser.invoke() }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
