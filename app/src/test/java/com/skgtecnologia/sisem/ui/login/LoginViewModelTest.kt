package com.skgtecnologia.sisem.ui.login

import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getLoginScreen: GetLoginScreen

    @MockK
    private lateinit var login: Login

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    private val screenModel = ScreenModel(body = emptyList())

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { androidIdProvider.getAndroidId() } returns "123"
        coEvery { getLoginScreen.invoke(any()) } returns Result.success(
            screenModel
        )
        loginViewModel = LoginViewModel(getLoginScreen, login, androidIdProvider)
    }

    @Test
    fun `when getLoginScreen is success`() = runTest {
        loginViewModel = LoginViewModel(getLoginScreen, login, androidIdProvider)
        val uiState = loginViewModel.uiState

        Assert.assertEquals(screenModel, uiState.screenModel)
        Assert.assertEquals(null, uiState.errorModel)
    }

    @Test
    fun `when getLoginScreen fails`() = runTest {
        val errorTitle = "Error en servidor"
        coEvery { getLoginScreen.invoke(any()) } returns Result.failure(
            IllegalStateException()
        )

        loginViewModel = LoginViewModel(getLoginScreen, login, androidIdProvider)
        val uiState = loginViewModel.uiState

        Assert.assertEquals(null, uiState.screenModel)
        Assert.assertEquals(errorTitle, uiState.errorModel?.title)
    }

    @Test
    fun `when call forgotPassword navigationModel should be forgotPassword true`() = runTest {
        loginViewModel.forgotPassword()

        Assert.assertEquals(true, loginViewModel.uiState.navigationModel?.forgotPassword)
    }

    @Test
    fun `when call login and validations are false don't should be authenticate`() = runTest {
        loginViewModel.login()

        Assert.assertEquals(false, loginViewModel.uiState.isLoading)
    }

    @Test
    fun `when call login and validations are true should be authenticate with warning`() = runTest {
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val accessTokenModel = createAccessToken(
            BannerModel(
                icon = "icon",
                title = "title",
                description = "description"
            )
        )

        coEvery { login.invoke(any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.navigationModel?.isWarning)
    }

    @Test
    fun `when call login and validations are true authenticate without warning`() = runTest {
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val accessTokenModel = createAccessToken(null)

        coEvery { login.invoke(any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel.login()

        Assert.assertEquals(false, loginViewModel.uiState.navigationModel?.isWarning)
    }

    @Test
    fun `when call login and validations are true authenticate fails`() = runTest {
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val errorTitle = "Error en servidor"

        coEvery { login.invoke(any(), any()) } returns Result.failure(IllegalStateException())

        loginViewModel.login()

        Assert.assertEquals(errorTitle, loginViewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call onNavigationHandled clear data`() = runTest {
        loginViewModel.onNavigationHandled()

        Assert.assertEquals(false, loginViewModel.uiState.validateFields)
        Assert.assertEquals(null, loginViewModel.uiState.navigationModel)
        Assert.assertEquals(false, loginViewModel.uiState.isLoading)
        Assert.assertEquals("", loginViewModel.password)
        Assert.assertEquals(false, loginViewModel.isValidPassword)
    }

    @Test
    fun `when call showLoginLink should be get loginLink`() = runTest {
        val loginLink = LoginLink.TERMS_AND_CONDITIONS
        loginViewModel.showLoginLink(loginLink)

        Assert.assertEquals(loginLink, loginViewModel.uiState.onLoginLink)
    }

    @Test
    fun `when call handleShownLoginLink should be clear onLoginLink`() = runTest {
        loginViewModel.handleShownLoginLink()

        Assert.assertEquals(null, loginViewModel.uiState.onLoginLink)
    }

    @Test
    fun `when call handleShownError should be clear errorModel`() = runTest {
        loginViewModel.handleShownError()

        Assert.assertEquals(null, loginViewModel.uiState.errorModel)
    }

    @Test
    fun `when call handleShownWarning should be clear warning`() = runTest {
        loginViewModel.handleShownWarning()

        Assert.assertEquals(null, loginViewModel.uiState.warning)
    }

    private fun createAccessToken(warning: BannerModel?) = AccessTokenModel(
        userId = 1,
        dateTime = LocalDateTime.now(),
        accessToken = "accessToken",
        refreshToken = "refreshToken",
        tokenType = "tokenType",
        username = "username",
        role = "role",
        isAdmin = true,
        nameUser = "nameUser",
        preoperational = null,
        turn = null,
        warning = warning,
        isWarning = false,
        docType = "docType",
        document = "document"
    )
}
