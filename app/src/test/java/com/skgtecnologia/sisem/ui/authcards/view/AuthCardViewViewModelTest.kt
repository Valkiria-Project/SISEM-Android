package com.skgtecnologia.sisem.ui.authcards.view

import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
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

class AuthCardViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getAuthCardViewScreen: GetAuthCardViewScreen

    private lateinit var viewModel: AuthCardViewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when call init and getAuthCardViewScreen is success`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = AuthCardViewViewModel(
            androidIdProvider,
            logoutCurrentUser,
            getAuthCardViewScreen
        )

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when call init and getAuthCardViewScreen fails`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.failure(
            IllegalStateException()
        )

        viewModel = AuthCardViewViewModel(
            androidIdProvider,
            logoutCurrentUser,
            getAuthCardViewScreen
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call goBack uiState should have back true`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = AuthCardViewViewModel(
            androidIdProvider,
            logoutCurrentUser,
            getAuthCardViewScreen
        )
        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when call navigate with assistant navigationModel role is assistant`() =
        runTest {
            coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

            viewModel = AuthCardViewViewModel(
                androidIdProvider,
                logoutCurrentUser,
                getAuthCardViewScreen
            )
            viewModel.navigate(PreOperationalViewIdentifier.CREW_MEMBER_CARD_ASSISTANT.name)

            Assert.assertEquals(
                OperationRole.AUXILIARY_AND_OR_TAPH,
                viewModel.uiState.navigationModel?.role
            )
        }

    @Test
    fun `when call navigate with driver navigationModel role is driver`() =
        runTest {
            coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

            viewModel = AuthCardViewViewModel(
                androidIdProvider,
                logoutCurrentUser,
                getAuthCardViewScreen
            )
            viewModel.navigate(PreOperationalViewIdentifier.CREW_MEMBER_CARD_DRIVER.name)

            Assert.assertEquals(
                OperationRole.DRIVER,
                viewModel.uiState.navigationModel?.role
            )
        }

    @Test
    fun `when call navigate with doctor navigationModel role is doctor`() =
        runTest {
            coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

            viewModel = AuthCardViewViewModel(
                androidIdProvider,
                logoutCurrentUser,
                getAuthCardViewScreen
            )
            viewModel.navigate(PreOperationalViewIdentifier.CREW_MEMBER_CARD_DOCTOR.name)

            Assert.assertEquals(
                OperationRole.MEDIC_APH,
                viewModel.uiState.navigationModel?.role
            )
        }

    @Test
    fun `when call navigate with unknown should fail with error model`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = AuthCardViewViewModel(
            androidIdProvider,
            logoutCurrentUser,
            getAuthCardViewScreen
        )
        viewModel.navigate("other")

        Assert.assertNotNull(viewModel.uiState.errorModel)
        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call onNavigationHandled uiState should have navigationModel clear`() = runTest {
        coEvery { getAuthCardViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = AuthCardViewViewModel(
            androidIdProvider,
            logoutCurrentUser,
            getAuthCardViewScreen
        )
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }
}
