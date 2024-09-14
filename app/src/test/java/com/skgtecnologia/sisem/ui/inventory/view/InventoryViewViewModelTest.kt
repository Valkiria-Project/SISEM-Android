package com.skgtecnologia.sisem.ui.inventory.view

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.OTHER
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceType
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.inventory.usecases.GetInventoryViewScreen
import com.skgtecnologia.sisem.domain.inventory.usecases.SaveTransferReturn
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InventoryViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var getInventoryViewScreen: GetInventoryViewScreen

    @MockK
    lateinit var getDeviceType: GetDeviceType

    @MockK
    lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    lateinit var saveTransferReturn: SaveTransferReturn

    @MockK
    lateinit var androidIdProvider: AndroidIdProvider

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = MainRoute.InventoryViewRoute(inventoryName = InventoryType.MEDICINE.type)
    )

    private lateinit var viewModel: InventoryViewViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getInventoryViewScreen is success`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewViewModel(
            savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            getDeviceType = getDeviceType,
            saveTransferReturn = saveTransferReturn,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getInventoryViewScreen is failure`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any(), any()) } returns Result.failure(Throwable())

        viewModel = InventoryViewViewModel(
            savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            getDeviceType = getDeviceType,
            saveTransferReturn = saveTransferReturn,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when inventoryType is not valid`() = runTest {
        val savedStateHandle = SavedStateHandle(
            route = MainRoute.InventoryViewRoute(inventoryName = OTHER)
        )

        viewModel = InventoryViewViewModel(
            savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            getDeviceType = getDeviceType,
            saveTransferReturn = saveTransferReturn,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when goBack is called`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewViewModel(
            savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            getDeviceType = getDeviceType,
            saveTransferReturn = saveTransferReturn,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewViewModel(
            savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            getDeviceType = getDeviceType,
            saveTransferReturn = saveTransferReturn,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when call handleEvent uiState should have errorModel clear`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any(), any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = InventoryViewViewModel(
            savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            getDeviceType = getDeviceType,
            saveTransferReturn = saveTransferReturn,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }
}
