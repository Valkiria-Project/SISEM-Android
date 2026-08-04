package com.skgtecnologia.sisem.ui.report.addreport

import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.report.usecases.GetAddReportRoleScreen
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

/**
 * The screen load is not covered here. It runs on `viewModelScope.launch(Dispatchers.IO)`,
 * and MainDispatcherRule only swaps out Main - so any assertion on it would be racing a
 * real background thread and would pass or fail on timing rather than on behaviour.
 * Covering it means injecting the dispatcher, which is a change to production code and
 * belongs in its own commit. Everything below runs through Main and is deterministic.
 */
class AddReportRoleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getAddReportRoleScreen: GetAddReportRoleScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    private lateinit var viewModel: AddReportRoleViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(UnauthorizedEventHandler)
        every { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) } just runs
        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
        coEvery { getAddReportRoleScreen.invoke(any()) } returns Result.success(emptyScreenModel)
    }

    @After
    fun teardown() {
        unmockkObject(UnauthorizedEventHandler)
    }

    private fun createViewModel() = AddReportRoleViewModel(
        getAddReportRoleScreen,
        logoutCurrentUser,
        androidIdProvider
    )

    @Test
    fun `the re-auth banner logs the user out and announces it`() = runTest {
        coEvery { logoutCurrentUser.invoke() } returns Result.success(USERNAME)
        viewModel = createViewModel()

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        coVerify { logoutCurrentUser.invoke() }
        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(USERNAME) }
    }

    @Test
    fun `a failed logout still announces it so the user is not stranded`() = runTest {
        coEvery { logoutCurrentUser.invoke() } returns Result.failure(Throwable("boom"))
        viewModel = createViewModel()

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
        )

        verify { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }

    @Test
    fun `any other action only dismisses the banner`() = runTest {
        viewModel = createViewModel()

        viewModel.handleEvent(GenericUiAction.DismissAction)

        Assert.assertEquals(null, viewModel.uiState.errorModel)
        coVerify(exactly = 0) { logoutCurrentUser.invoke() }
        verify(exactly = 0) { UnauthorizedEventHandler.publishUnauthorizedEvent(any()) }
    }
}
