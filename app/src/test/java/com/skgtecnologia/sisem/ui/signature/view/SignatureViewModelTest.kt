package com.skgtecnologia.sisem.ui.signature.view

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.DOCUMENT
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.core.navigation.MainRoute
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.signature.usecases.GetSignatureScreen
import com.skgtecnologia.sisem.domain.signature.usecases.RegisterSignature
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
class SignatureViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var geSignatureScreen: GetSignatureScreen

    @MockK
    private lateinit var registerSignature: RegisterSignature

    @MockK
    private lateinit var logoutCurrentUser: LogoutCurrentUser

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = MainRoute.SignatureRoute(DOCUMENT)
    )

    private lateinit var signatureViewModel: SignatureViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when geSignatureScreen is success`() = runTest {
        coEvery { geSignatureScreen.invoke(DOCUMENT) } returns Result.success(
            emptyScreenModel
        )

        signatureViewModel = SignatureViewModel(
            savedStateHandle,
            geSignatureScreen,
            registerSignature,
            logoutCurrentUser
        )
        val uiState = signatureViewModel.uiState

        Assert.assertEquals(emptyScreenModel, uiState.screenModel)
        Assert.assertEquals(null, uiState.errorModel)
    }

    @Test
    fun `when geSignatureScreen fails`() = runTest {
        coEvery { geSignatureScreen.invoke(any()) } returns Result.failure(
            Throwable()
        )

        signatureViewModel = SignatureViewModel(
            savedStateHandle,
            geSignatureScreen,
            registerSignature,
            logoutCurrentUser
        )
        val uiState = signatureViewModel.uiState

        Assert.assertEquals(null, uiState.screenModel)
        Assert.assertEquals(SERVER_ERROR_TITLE, uiState.errorModel?.title)
    }
}
