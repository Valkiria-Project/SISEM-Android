package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.skgtecnologia.sisem.domain.operation.usecases.LogoutTurn
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val USERNAME = "username"

class MenuViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getAllAccessTokens: GetAllAccessTokens

    @MockK
    private lateinit var observeOperationConfig: ObserveOperationConfig

    @MockK
    private lateinit var logout: Logout

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var logoutTurn: LogoutTurn

    private lateinit var viewModel: MenuViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getAllAccessTokens failure`() = runTest {
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.failure(Throwable())
        coEvery { observeOperationConfig.invoke() } returns flowOf(operationConfig)

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            observeOperationConfig = observeOperationConfig
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.operationConfig.collect()
        }

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.value.errorModel?.title)
    }

    @Test
    fun `when getAllAccessTokens and observeOperationConfig are success`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns flowOf(operationConfig)

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            observeOperationConfig = observeOperationConfig
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.operationConfig.collect()
        }

        Assert.assertEquals(vehicleConfigModel, viewModel.uiState.value.vehicleConfig)
        Assert.assertEquals(accessTokens, viewModel.uiState.value.accessTokenModelList)
    }

    @Test
    fun `when getAllAccessTokens is success and observeOperationConfig failure`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.failure(IllegalStateException())
        coEvery { observeOperationConfig.invoke() } returns flowOf()

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            observeOperationConfig = observeOperationConfig
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.operationConfig.collect()
        }

        Assert.assertEquals(null, viewModel.uiState.value.accessTokenModelList)
        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.value.errorModel?.title)
    }

    @Test
    fun `when logout is success`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns flowOf(operationConfig)
        coEvery { logoutTurn.invoke(USERNAME) } returns Result.success("")

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            observeOperationConfig = observeOperationConfig
        )

        viewModel.logout(USERNAME)

        Assert.assertEquals(true, viewModel.uiState.value.isLogout)
    }

    @Test
    fun `when logout is failure`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns flowOf(operationConfig)
        coEvery { logoutTurn.invoke(USERNAME) } returns Result.failure(Throwable())

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            observeOperationConfig = observeOperationConfig
        )

        viewModel.logout(USERNAME)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.value.errorModel?.title)
    }

    @Test
    fun `when handleEvent is called`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns flowOf(operationConfig)
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            observeOperationConfig = observeOperationConfig
        )

        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.value.errorModel)
    }
}
