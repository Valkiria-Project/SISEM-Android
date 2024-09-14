package com.skgtecnologia.sisem.ui.stretcherretention.view

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.ID_APH
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.GetStretcherRetentionViewScreen
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StretcherRetentionViewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    @MockK
    private lateinit var getStretcherRetentionViewScreen: GetStretcherRetentionViewScreen

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = MainRoute.StretcherRetentionViewRoute(idAph = ID_APH)
    )

    private lateinit var stretcherRetentionViewViewModel: StretcherRetentionViewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when getStretcherRetentionViewScreen is success`() = runTest {
        coEvery { getStretcherRetentionViewScreen.invoke(ID_APH) } returns Result.success(
            emptyScreenModel
        )

        stretcherRetentionViewViewModel = StretcherRetentionViewViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionViewScreen
        )
        val uiState = stretcherRetentionViewViewModel.uiState

        Assert.assertEquals(emptyScreenModel, uiState.screenModel)
        Assert.assertEquals(null, uiState.infoEvent)
    }

    @Test
    fun `when getStretcherRetentionViewScreen fails`() = runTest {
        coEvery { getStretcherRetentionViewScreen.invoke(ID_APH) } returns Result.failure(
            Throwable()
        )

        stretcherRetentionViewViewModel = StretcherRetentionViewViewModel(
            savedStateHandle,
            logoutCurrentUser,
            getStretcherRetentionViewScreen
        )
        val uiState = stretcherRetentionViewViewModel.uiState

        Assert.assertEquals(null, uiState.screenModel)
        Assert.assertEquals(SERVER_ERROR_TITLE, uiState.infoEvent?.title)
    }
}
