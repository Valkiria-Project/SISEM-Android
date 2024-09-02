package com.skgtecnologia.sisem.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
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
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getLoginScreen: GetLoginScreen

    @MockK
    private lateinit var login: Login

    @MockK
    private lateinit var androidIdProvider: AndroidIdProvider

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(
        route = AuthRoute.LoginRoute(username = USERNAME)
    )

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
    }

    @Test
    fun `when getLoginScreen is success`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        val uiState = loginViewModel.uiState

        Assert.assertEquals(emptyScreenModel, uiState.screenModel)
        Assert.assertEquals(null, uiState.errorModel)
    }

    @Test
    fun `when getLoginScreen fails`() = runTest {
        coEvery { getLoginScreen.invoke(any()) } returns Result.failure(
            Throwable()
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        val uiState = loginViewModel.uiState

        Assert.assertEquals(null, uiState.screenModel)
        Assert.assertEquals(SERVER_ERROR_TITLE, uiState.errorModel?.title)
    }

    @Test
    fun `when call forgotPassword navigationModel should have forgotPassword true`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.forgotPassword()

        Assert.assertEquals(true, loginViewModel.uiState.navigationModel?.forgotPassword)
    }

    @Test
    fun `when call login and validations fail`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.validateFields)
        Assert.assertEquals(false, loginViewModel.uiState.isLoading)
    }

    @Test
    fun `when call login and validations succeed authenticate with warning`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)

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

        Assert.assertEquals(true, loginViewModel.uiState.validateFields)
        Assert.assertEquals(true, loginViewModel.uiState.navigationModel?.isWarning)
        Assert.assertEquals(false, loginViewModel.uiState.isLoading)
    }

    @Test
    fun `when call login and validations succeed authenticate without warning`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val accessTokenModel = createAccessToken(null)

        coEvery { login.invoke(any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.validateFields)
        Assert.assertEquals(false, loginViewModel.uiState.navigationModel?.isWarning)
        Assert.assertEquals(true, loginViewModel.uiState.isLoading)
    }

    @Test
    fun `when call login and validations succeed authenticate  with error`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true

        coEvery { login.invoke(any(), any()) } returns Result.failure(Throwable())

        loginViewModel.login()

        Assert.assertEquals(SERVER_ERROR_TITLE, loginViewModel.uiState.errorModel?.title)
    }

    @Test
    fun `when call consumeNavigationEvent clear data`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.consumeNavigationEvent()

        Assert.assertEquals(false, loginViewModel.uiState.validateFields)
        Assert.assertEquals(null, loginViewModel.uiState.navigationModel)
        Assert.assertEquals(false, loginViewModel.uiState.isLoading)
        Assert.assertEquals("", loginViewModel.password)
        Assert.assertEquals(false, loginViewModel.isValidPassword)
    }

    @Test
    fun `when call showLoginLink uiState should have onLoginLink`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        val loginLink = LoginLink.TERMS_AND_CONDITIONS
        loginViewModel.showLoginLink(loginLink)

        Assert.assertEquals(loginLink, loginViewModel.uiState.onLoginLink)
    }

    @Test
    fun `when call consumeLoginLinkEvent uiState should have onLoginLink clear `() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.consumeLoginLinkEvent()

        Assert.assertEquals(null, loginViewModel.uiState.onLoginLink)
    }

    @Test
    fun `when call consumeErrorEvent uiState should have errorModel clear`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.consumeErrorEvent()

        Assert.assertEquals(null, loginViewModel.uiState.errorModel)
    }

    @Test
    fun `when call consumeWarningEvent uiState should have warning clear`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = LoginViewModel(savedStateHandle, androidIdProvider, getLoginScreen, login)
        loginViewModel.consumeWarningEvent()

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
        document = "document",
        refreshDateTime = LocalDateTime.now(),
        expDate = LocalDateTime.now()
    )
}
