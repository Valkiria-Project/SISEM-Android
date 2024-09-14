package com.skgtecnologia.sisem.ui.stretcherretention.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.commons.uiAction
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.GetStretcherRetentionScreen
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.SaveStretcherRetention
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
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

private const val BANNER_TITLE = "Retención de camilla"

@RunWith(RobolectricTestRunner::class)
class StretcherRetentionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getStretcherRetentionScreen: GetStretcherRetentionScreen

    @MockK
    private lateinit var saveStretcherRetention: SaveStretcherRetention

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = MainRoute.StretcherRetentionRoute(idAph = ID_APH)
    )

    private lateinit var viewModel: StretcherRetentionViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getStretcherRetentionScreen is success`() = runTest {
        val screenModel = ScreenModel(
            body = listOf(
                TextFieldUiModel(
                    identifier = "identifier",
                    keyboardOptions = KeyboardOptions.Default,
                    textStyle = TextStyle.BODY_1,
                    charLimit = 10,
                    validations = listOf(),
                    arrangement = Arrangement.Start,
                    modifier = Modifier
                )
            )
        )
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )

        Assert.assertEquals(screenModel, viewModel.uiState.screenModel)
    }

    @Test
    fun `when getStretcherRetentionScreen is failure`() = runTest {
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.failure(
            Throwable()
        )

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }

    @Test
    fun `when consumeNavigationEvent is called clear uiState`() = runTest {
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )
        viewModel.consumeNavigationEvent()

        Assert.assertEquals(false, viewModel.uiState.isLoading)
        Assert.assertEquals(false, viewModel.uiState.validateFields)
        Assert.assertEquals(null, viewModel.uiState.navigationModel)
    }

    @Test
    fun `when handleEvent is called clear uiState`() = runTest {
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery { logoutCurrentUser.invoke() } returns Result.success("")

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )
        viewModel.handleEvent(uiAction)

        Assert.assertEquals(null, viewModel.uiState.infoEvent)
    }

    @Test
    fun `when goBack is called clear successEvent and navigate back`() = runTest {
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )
        viewModel.gpBack()

        Assert.assertEquals(null, viewModel.uiState.successEvent)
        Assert.assertEquals(true, viewModel.uiState.navigationModel?.back)
    }

    @Test
    fun `when saveRetention is called validate fields and save retention success`() = runTest {
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery {
            saveStretcherRetention.invoke(
                ID_APH,
                any(),
                any()
            )
        } returns Result.success(Unit)

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )
        viewModel.saveRetention()

        Assert.assertEquals(BANNER_TITLE, viewModel.uiState.successEvent?.title)
    }

    @Test
    fun `when saveRetention is called validate fields and save retention failure`() = runTest {
        coEvery { getStretcherRetentionScreen.invoke(any()) } returns Result.success(
            emptyScreenModel
        )
        coEvery {
            saveStretcherRetention.invoke(
                ID_APH,
                any(),
                any()
            )
        } returns Result.failure(Throwable())

        viewModel = StretcherRetentionViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionScreen,
            saveStretcherRetention
        )
        viewModel.saveRetention()

        Assert.assertEquals(SERVER_ERROR_TITLE, viewModel.uiState.infoEvent?.title)
    }
}
