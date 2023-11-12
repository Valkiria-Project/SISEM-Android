package com.skgtecnologia.sisem.ui.inventory.view

import androidx.lifecycle.SavedStateHandle
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.inventory.usecases.GetInventoryViewScreen
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.INVENTORY_TYPE
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InventoryViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var getInventoryViewScreen: GetInventoryViewScreen

    @MockK
    lateinit var logoutCurrentUser: LogoutCurrentUser

    private val savedStateHandle = SavedStateHandle(
        mapOf(INVENTORY_TYPE to InventoryType.MEDICINE.type)
    )

    private lateinit var viewModel: InventoryViewViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getInventoryViewScreen is success`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = InventoryViewViewModel(
            savedStateHandle = savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            logoutCurrentUser = logoutCurrentUser
        )

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getInventoryViewScreen is failure`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any()) } returns Result.failure(Throwable())

        viewModel = InventoryViewViewModel(
            savedStateHandle = savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            logoutCurrentUser = logoutCurrentUser
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when inventoryType is null`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf(INVENTORY_TYPE to null))

        viewModel = InventoryViewViewModel(
            savedStateHandle = savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            logoutCurrentUser = logoutCurrentUser
        )

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when goBack is called`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = InventoryViewViewModel(
            savedStateHandle = savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            logoutCurrentUser = logoutCurrentUser
        )

        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when consumeNavigationEvent is called`() = runTest {
        coEvery { getInventoryViewScreen.invoke(any()) } returns Result.success(emptyScreenModel)

        viewModel = InventoryViewViewModel(
            savedStateHandle = savedStateHandle,
            getInventoryViewScreen = getInventoryViewScreen,
            logoutCurrentUser = logoutCurrentUser
        )

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }
}
