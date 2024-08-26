package com.skgtecnologia.sisem.ui.preoperational.create

import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.DRIVER_ROLE
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.chipOptionsUiModelMock
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.findingUiModelMock
import com.skgtecnologia.sisem.commons.inventoryCheckUiModelMock
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.textFieldUiModelMock
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreen
import com.skgtecnologia.sisem.domain.preoperational.usecases.SendPreOperational
import com.skgtecnologia.sisem.ui.login.LoginNavigationModel
import com.valkiria.uicomponents.action.GenericUiAction
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

private const val BANNER_CONFIRMATION_TITLE = "Guardar cambios"
private const val BANNER_INCOMPLETE_TITLE = "Incompleto"

class PreOperationalViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var getLoginNavigationModel: GetLoginNavigationModel

    @MockK
    private lateinit var getPreOperationalScreen: GetPreOperationalScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var observeOperationConfig: ObserveOperationConfig

    @MockK
    private lateinit var sendPreOperational: SendPreOperational

    private lateinit var viewModel: PreOperationalViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when observeOperationConfig and getPreOperationalScreen are success`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                chipOptionsUiModelMock,
                findingUiModelMock,
                inventoryCheckUiModelMock,
                textFieldUiModelMock
            )
        )
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            screenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.initScreen(DRIVER_ROLE)

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
        Assert.assertEquals(operationModel, viewModel.uiState.operationModel)
    }

    @Test
    fun `when observeOperationConfig is success and getPreOperationalScreen is failure`() =
        runTest {
            val operationModel = mockk<OperationModel>()
            coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
            coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.failure(
                Throwable()
            )

            viewModel = PreOperationalViewModel(
                androidIdProvider,
                getLoginNavigationModel,
                getPreOperationalScreen,
                logoutCurrentUser,
                observeOperationConfig,
                sendPreOperational
            )
            viewModel.initScreen(DRIVER_ROLE)

            Assert.assertEquals(operationModel, viewModel.uiState.operationModel)
            Assert.assertEquals(null, viewModel.uiState.screenModel)
            Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
        }

    @Test
    fun `when observeOperationConfig is failure and getPreOperationalScreen is success`() =
        runTest {
            coEvery { observeOperationConfig.invoke() } returns Result.failure(Throwable())
            coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
                emptyScreenModel
            )

            viewModel = PreOperationalViewModel(
                androidIdProvider,
                getLoginNavigationModel,
                getPreOperationalScreen,
                logoutCurrentUser,
                observeOperationConfig,
                sendPreOperational
            )
            viewModel.initScreen(DRIVER_ROLE)

            Assert.assertEquals(null, viewModel.uiState.operationModel)
            Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
            Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
        }

    @Test
    fun `when observeOperationConfig and getPreOperationalScreen are failure`() = runTest {
        coEvery { observeOperationConfig.invoke() } returns Result.failure(Throwable())
        coEvery {
            getPreOperationalScreen.invoke(DRIVER_ROLE, any())
        } returns Result.failure(Throwable())

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.initScreen(DRIVER_ROLE)

        Assert.assertEquals(null, viewModel.uiState.operationModel)
        Assert.assertEquals(null, viewModel.uiState.screenModel)
        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when handleFindingAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                findingUiModelMock,
                textFieldUiModelMock
            )
        )
        val identifier = "identifier"
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            screenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )

        viewModel.handleFindingAction(
            GenericUiAction.FindingAction(
                identifier = identifier,
                status = false,
                findingDetail = null
            )
        )

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isNewFinding)
        Assert.assertEquals(identifier, viewModel.uiState.navigationModel?.findingId)
    }

    @Test
    fun `when handleInputAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                textFieldUiModelMock,
                textFieldUiModelMock.copy(identifier = "other")
            )
        )
        val identifier = "identifier"
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            screenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.initScreen(DRIVER_ROLE)

        viewModel.handleInputAction(
            GenericUiAction.InputAction(
                identifier = identifier,
                updatedValue = "updatedValue",
                fieldValidated = false,
                required = false
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when handleInventoryAction is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                inventoryCheckUiModelMock
            )
        )
        val identifier = "identifier"
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            screenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.initScreen(DRIVER_ROLE)

        viewModel.handleInventoryAction(
            GenericUiAction.InventoryAction(
                identifier = identifier,
                itemIdentifier = "itemIdentifier",
                updatedValue = "updatedValue",
                fieldValidated = false
            )
        )

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when revertFinding is called`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                findingUiModelMock,
                textFieldUiModelMock
            )
        )
        val identifier = "identifier"
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            screenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.initScreen(DRIVER_ROLE)

        viewModel.handleFindingAction(
            GenericUiAction.FindingAction(
                identifier = identifier,
                status = false,
                findingDetail = null
            )
        )

        viewModel.revertFinding()

        Assert.assertNotNull(viewModel.uiState.screenModel)
    }

    @Test
    fun `when savePreOperational is called and validations are true`() = runTest {
        val vehicleConfigModel = mockk<VehicleConfigModel> {
            every { zone } returns "zone"
        }
        val operationModel = mockk<OperationModel> {
            every { vehicleConfig } returns vehicleConfigModel
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )

        viewModel.savePreOperational()

        Assert.assertEquals(BANNER_CONFIRMATION_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when savePreOperational is called and validations are false`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                inventoryCheckUiModelMock,
                textFieldUiModelMock
            )
        )
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            screenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.initScreen(DRIVER_ROLE)

        viewModel.savePreOperational()

        Assert.assertEquals(BANNER_INCOMPLETE_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendPreOperational and getLoginNavigationModel are success`() = runTest {
        val operationModel = mockk<OperationModel>()
        val loginNavigationModel = mockk<LoginNavigationModel> {
            every { isTurnComplete } returns true
        }
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery {
            sendPreOperational.invoke(
                DRIVER_ROLE,
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(
            Unit
        )
        coEvery { getLoginNavigationModel.invoke() } returns Result.success(loginNavigationModel)

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )

        viewModel.sendPreOperational(DRIVER_ROLE)

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.isTurnComplete)
    }

    @Test
    fun `when sendPreOperational is success and getLoginNavigationModel failure`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery {
            sendPreOperational.invoke(
                DRIVER_ROLE, any(), any(), any(), any()
            )
        } returns Result.success(
            Unit
        )
        coEvery { getLoginNavigationModel.invoke() } returns Result.failure(IllegalStateException())

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )

        viewModel.sendPreOperational(DRIVER_ROLE)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when sendPreOperational is failure`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery {
            sendPreOperational.invoke(
                DRIVER_ROLE, any(), any(), any(), any()
            )
        } returns Result.failure(
            IllegalStateException()
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.sendPreOperational(DRIVER_ROLE)

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when consumeInfoEvent is called`() = runTest {
        val operationModel = mockk<OperationModel>()
        coEvery { observeOperationConfig.invoke() } returns Result.success(operationModel)
        coEvery { getPreOperationalScreen.invoke(DRIVER_ROLE, any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = PreOperationalViewModel(
            androidIdProvider,
            getLoginNavigationModel,
            getPreOperationalScreen,
            logoutCurrentUser,
            observeOperationConfig,
            sendPreOperational
        )
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }
}
