package com.skgtecnologia.sisem.ui.inventory

import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.inventory.usecases.GetInventoryInitialScreen
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.ui.authcards.view.AuthCardViewViewModel
import com.valkiria.uicomponents.action.FooterUiAction
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InventoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var getInventoryInitialScreen: GetInventoryInitialScreen

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    private lateinit var viewModel: InventoryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getInventoryInitialScreen is success`() = runTest {
        coEvery { getInventoryInitialScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewModel(
            getInventoryInitialScreen = getInventoryInitialScreen,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        Assert.assertEquals(emptyScreenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getInventoryInitialScreen is failure`() = runTest {
        coEvery { getInventoryInitialScreen.invoke(ANDROID_ID) } returns Result.failure(
            Throwable()
        )

        viewModel = InventoryViewModel(
            getInventoryInitialScreen = getInventoryInitialScreen,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when navigate is called`() {
        val identifier = "identifier"
        coEvery { getInventoryInitialScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewModel(
            getInventoryInitialScreen = getInventoryInitialScreen,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )
        viewModel.navigate(identifier)

        Assert.assertEquals(identifier, viewModel.uiState.navigationModel?.identifier)
    }

    @Test
    fun `when goBack is called`() {
        coEvery { getInventoryInitialScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewModel(
            getInventoryInitialScreen = getInventoryInitialScreen,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )
        viewModel.goBack()

        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when consumeNavigationEvent is called`() {
        coEvery { getInventoryInitialScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        viewModel = InventoryViewModel(
            getInventoryInitialScreen = getInventoryInitialScreen,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )

        viewModel.consumeNavigationEvent()

        Assert.assertEquals(null, viewModel.uiState.navigationModel)
        Assert.assertEquals(false, viewModel.uiState.isLoading)
    }

    @Test
    fun `when call handleEvent uiState should have errorModel clear`() = runTest {
        coEvery { getInventoryInitialScreen.invoke(any()) } returns Result.success(emptyScreenModel)
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = InventoryViewModel(
            getInventoryInitialScreen = getInventoryInitialScreen,
            logoutCurrentUser = logoutCurrentUser,
            androidIdProvider = androidIdProvider
        )
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.errorModel)
    }
}
