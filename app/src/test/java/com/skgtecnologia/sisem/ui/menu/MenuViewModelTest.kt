package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.data.offline.outbox.FakeOutboxDao
import com.skgtecnologia.sisem.data.offline.outbox.OutboxEntity
import com.skgtecnologia.sisem.data.offline.outbox.OutboxStore
import com.skgtecnologia.sisem.data.offline.screen.ReversingCipher
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.LogoutIdentifier
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.skgtecnologia.sisem.domain.operation.usecases.LogoutTurn
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.valkiria.uicomponents.action.FooterUiAction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

private const val USERNAME = "username"

@RunWith(RobolectricTestRunner::class)
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

    private val outboxDao = FakeOutboxDao()
    private val outboxStore = OutboxStore(outboxDao, File("unused"), ReversingCipher())

    private lateinit var viewModel: MenuViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getAllAccessTokens failure`() = runTest {
        coEvery { getAllAccessTokens.invoke() } returns Result.failure(Throwable())
        coEvery { observeOperationConfig.invoke() } returns MutableSharedFlow()

        viewModel = MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            outboxStore = outboxStore,
            observeOperationConfig = observeOperationConfig
        )

        val job = backgroundScope.launch {
            viewModel.operationConfig.collect()
        }

        viewModel.uiState.first { it.errorModel != null }

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.value.errorModel?.title)
        job.cancel()
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
            outboxStore = outboxStore,
            observeOperationConfig = observeOperationConfig
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.operationConfig.collect()
        }
        advanceUntilIdle()

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
            outboxStore = outboxStore,
            observeOperationConfig = observeOperationConfig
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.operationConfig.collect()
        }
        advanceUntilIdle()

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
            outboxStore = outboxStore,
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
            outboxStore = outboxStore,
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
            outboxStore = outboxStore,
            observeOperationConfig = observeOperationConfig
        )

        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.value.errorModel)
    }

    private fun menuViewModelWithPendingWrites(): MenuViewModel {
        coEvery { getAllAccessTokens.invoke() } returns Result.success(emptyList())
        coEvery { observeOperationConfig.invoke() } returns MutableSharedFlow()
        coEvery { logoutTurn.invoke(any()) } returns Result.success("")
        kotlinx.coroutines.runBlocking {
            outboxDao.insert(pendingWrite("a", createdBy = USERNAME))
            outboxDao.insert(pendingWrite("b", createdBy = USERNAME))
            outboxDao.insert(pendingWrite("c", createdBy = "someone-else"))
        }

        return MenuViewModel(
            getAllAccessTokens = getAllAccessTokens,
            logout = logout,
            logoutCurrentUser = logoutCurrentUser,
            logoutTurn = logoutTurn,
            outboxStore = outboxStore,
            observeOperationConfig = observeOperationConfig
        )
    }

    private fun pendingWrite(id: String, createdBy: String) = OutboxEntity(
        requestId = id,
        idempotencyKey = id,
        method = "POST",
        url = "https://api.example.test/aph",
        headers = "",
        contentType = null,
        hasBody = false,
        createdBy = createdBy,
        createdAt = 0
    )

    @Test
    fun `logging out with unsent writes asks first`() = runTest {
        viewModel = menuViewModelWithPendingWrites()

        viewModel.logout(USERNAME)

        Assert.assertEquals("Registros sin enviar", viewModel.uiState.value.errorModel?.title)
        Assert.assertTrue(viewModel.uiState.value.errorModel?.description.orEmpty().contains("2 registros"))
        coVerify(exactly = 0) { logoutTurn.invoke(any()) }
    }

    @Test
    fun `confirming logs out anyway`() = runTest {
        viewModel = menuViewModelWithPendingWrites()
        viewModel.logout(USERNAME)

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LogoutIdentifier.LOGOUT_PENDING_WRITES_CONTINUE_BANNER.name)
        )

        coVerify(exactly = 1) { logoutTurn.invoke(USERNAME) }
        Assert.assertEquals(true, viewModel.uiState.value.isLogout)
    }

    @Test
    fun `cancelling keeps the session`() = runTest {
        viewModel = menuViewModelWithPendingWrites()
        viewModel.logout(USERNAME)

        viewModel.handleEvent(
            FooterUiAction.FooterButton(LogoutIdentifier.LOGOUT_PENDING_WRITES_CANCEL_BANNER.name)
        )

        coVerify(exactly = 0) { logoutTurn.invoke(any()) }
        Assert.assertEquals(null, viewModel.uiState.value.errorModel)
    }

    @Test
    fun `someone else's unsent writes do not hold up this logout`() = runTest {
        viewModel = menuViewModelWithPendingWrites()

        viewModel.logout("someone-without-writes")

        Assert.assertEquals(null, viewModel.uiState.value.errorModel)
        coVerify(exactly = 1) { logoutTurn.invoke("someone-without-writes") }
    }
}
