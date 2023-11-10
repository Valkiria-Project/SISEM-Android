package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
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

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getLoginScreen: GetLoginScreen

    @MockK
    private lateinit var login: Login

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { androidIdProvider.getAndroidId() } returns "123"
        loginViewModel = LoginViewModel(getLoginScreen, login, androidIdProvider)
    }

    @Test
    fun `when getLoginScreen is success`() = runTest {
        val screenModel = mockk<ScreenModel>()
        coEvery { getLoginScreen.invoke(any()) } returns Result.success(
            screenModel
        )

        loginViewModel = LoginViewModel(getLoginScreen, login, androidIdProvider)
        val uiState = loginViewModel.uiState

        Assert.assertEquals(screenModel, uiState.screenModel)
        Assert.assertEquals(null, uiState.errorModel)
    }

    @Test
    fun `when getLoginScreen fails`() = runTest {
        val errorTitle = "Error en servidor"
        coEvery { getLoginScreen.invoke(androidIdProvider.getAndroidId()) } returns Result.failure(
            IllegalStateException()
        )

        val uiState = loginViewModel.uiState

        Assert.assertEquals(null, uiState.screenModel)
        Assert.assertEquals(errorTitle, uiState.errorModel?.title)
    }

    @Test
    fun `when call forgotPassword navigationModel should be forgotPassword true`() {
        loginViewModel.forgotPassword()

        Assert.assertEquals(true, loginViewModel.uiState.navigationModel?.forgotPassword)
    }

    @Test
    fun `when call login and validations are false don't should be authenticate`() {
        loginViewModel.login()

        Assert.assertEquals(false, loginViewModel.uiState.isLoading)
    }

    @Test
    fun `when call login and validations are true should be authenticate with warning`() {
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val accessTokenModel = mockk<AccessTokenModel> {
            every { warning } returns mockk()
        }

        coEvery { login.invoke(any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.navigationModel?.isWarning)
    }
}
