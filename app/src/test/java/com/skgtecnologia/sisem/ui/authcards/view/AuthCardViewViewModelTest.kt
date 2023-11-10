package com.skgtecnologia.sisem.ui.authcards.view

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalViewIdentifier
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetAuthCardViewScreen
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val ANDROID_ID = "123"
private const val SERVER_ERROR_TITLE = "Error en servidor"

class AuthCardViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getAuthCardViewScreen: GetAuthCardViewScreen

    private val screenModel = ScreenModel(body = emptyList())

    private lateinit var viewModel: AuthCardViewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when init view model getAuthCardViewScreen is success`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when init view model getAuthCardViewScreen fails`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.failure(
            IllegalStateException()
        )

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when goBack is called navigationModel back is true`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when navigate is called with assistant identifier navigationModel role is correct`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        viewModel.navigate(PreOperationalViewIdentifier.CREW_MEMBER_CARD_ASSISTANT.name)

        Assert.assertEquals(
            OperationRole.AUXILIARY_AND_OR_TAPH,
            viewModel.uiState.navigationModel?.role
        )
    }

    @Test
    fun `when navigate is called with driver identifier navigationModel role is correct`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        viewModel.navigate(PreOperationalViewIdentifier.CREW_MEMBER_CARD_DRIVER.name)

        Assert.assertEquals(
            OperationRole.DRIVER,
            viewModel.uiState.navigationModel?.role
        )
    }

    @Test
    fun `when navigate is called with doctor identifier navigationModel role is correct`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        viewModel.navigate(PreOperationalViewIdentifier.CREW_MEMBER_CARD_DOCTOR.name)

        Assert.assertEquals(
            OperationRole.MEDIC_APH,
            viewModel.uiState.navigationModel?.role
        )
    }

    @Test
    fun `when navigate is called with unknown identifier failure and error model is not null`() = runTest {
        val unknownIdentifier = "other"
        val errorMessage = "Identifier $unknownIdentifier not supported"
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        viewModel.navigate("other")

        Assert.assertNotNull(viewModel.uiState.errorModel)
        Assert.assertEquals(errorMessage, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when onNavigationHandled is called clear uiState`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(screenModel)

        viewModel =
            AuthCardViewViewModel(androidIdProvider, logoutCurrentUser, getAuthCardViewScreen)

        viewModel.onNavigationHandled()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }
}
