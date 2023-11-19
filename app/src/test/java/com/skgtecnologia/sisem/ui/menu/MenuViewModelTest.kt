package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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

    private lateinit var viewModel: MenuViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getAllAccessTokens failure`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.failure(Throwable())

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            observeOperationConfig = observeOperationConfig,
            logout = logout
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when getAllAccessTokens and observeOperationConfig are success`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationConfig)

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            observeOperationConfig = observeOperationConfig,
            logout = logout
        )

        Assert.assertEquals(vehicleConfigModel, viewModel.uiState.vehicleConfig)
        Assert.assertEquals(accessTokens, viewModel.uiState.accessTokenModelList)
    }

    @Test
    fun `when getAllAccessTokens is success and observeOperationConfig failure`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns Result.failure(IllegalStateException())

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            observeOperationConfig = observeOperationConfig,
            logout = logout
        )

        Assert.assertEquals(null, viewModel.uiState.accessTokenModelList)
        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when logout is success`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationConfig)
        coEvery { logout.invoke(USERNAME) } returns Result.success("")

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            observeOperationConfig = observeOperationConfig,
            logout = logout
        )

        viewModel.logout(USERNAME)

        Assert.assertEquals(true, viewModel.uiState.isLogout)
    }

    @Test
    fun `when logout is failure`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationConfig)
        coEvery { logout.invoke(USERNAME) } returns Result.failure(Throwable())

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            observeOperationConfig = observeOperationConfig,
            logout = logout
        )

        viewModel.logout(USERNAME)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when consumeErrorEvent is called`() = runTest {
        val accessTokens = listOf(mockk<AccessTokenModel>())
        val vehicleConfigModel = mockk<VehicleConfigModel>()
        val operationConfig = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { getAllAccessTokens.invoke() } returns Result.success(accessTokens)
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationConfig)

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            observeOperationConfig = observeOperationConfig,
            logout = logout
        )

        viewModel.consumeErrorEvent()

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }
}
