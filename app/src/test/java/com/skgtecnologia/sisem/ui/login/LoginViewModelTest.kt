package com.skgtecnologia.sisem.ui.login

import androidx.lifecycle.SavedStateHandle
import com.skgtecnologia.sisem.commons.ANDROID_ID
import com.skgtecnologia.sisem.commons.MainDispatcherRule
import com.skgtecnologia.sisem.commons.PASSWORD
import com.skgtecnologia.sisem.commons.SERVER_ERROR_TITLE
import com.skgtecnologia.sisem.commons.USERNAME
import com.skgtecnologia.sisem.commons.biometric.FaceCredentialStore
import com.skgtecnologia.sisem.commons.emptyScreenModel
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.biometric.usecases.GetLoginCredentials
import com.skgtecnologia.sisem.domain.biometric.usecases.PurgeStaleBiometrics
import com.skgtecnologia.sisem.domain.biometric.usecases.StoreBiometricFromLogin
import com.skgtecnologia.sisem.domain.biometric.usecases.StoreLoginCredentials
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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

    @MockK
    private lateinit var faceCredentialStore: FaceCredentialStore

    @MockK
    private lateinit var storeBiometricFromLogin: StoreBiometricFromLogin

    @MockK
    private lateinit var storeLoginCredentials: StoreLoginCredentials

    @MockK
    private lateinit var getLoginCredentials: GetLoginCredentials

    @MockK
    private lateinit var purgeStaleBiometrics: PurgeStaleBiometrics

    private val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
        set("username", USERNAME)
    }

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { androidIdProvider.getAndroidId() } returns ANDROID_ID
        coEvery { faceCredentialStore.storeRefreshToken(any(), any()) } returns Unit
        coEvery { faceCredentialStore.storeRole(any(), any()) } returns Unit
        coEvery { faceCredentialStore.enrolledUsernames() } returns emptyList()
        coEvery { storeBiometricFromLogin.invoke(any(), any(), any(), any()) } returns Unit
        coEvery { storeLoginCredentials.invoke(any(), any()) } returns Unit
        coEvery { getLoginCredentials.invoke(any()) } returns null
        coEvery { purgeStaleBiometrics.invoke() } returns Unit
    }

    private fun createViewModel() = LoginViewModel(
        savedStateHandle = savedStateHandle,
        androidIdProvider = androidIdProvider,
        getLoginScreen = getLoginScreen,
        login = login,
        faceCredentialStore = faceCredentialStore,
        storeBiometricFromLogin = storeBiometricFromLogin,
        storeLoginCredentials = storeLoginCredentials,
        getLoginCredentials = getLoginCredentials,
        purgeStaleBiometrics = purgeStaleBiometrics
    )

    @Test
    fun `when getLoginScreen is success`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        val uiState = loginViewModel.uiState

        Assert.assertEquals(emptyScreenModel, uiState.value.screenModel)
        Assert.assertEquals(null, uiState.value.errorModel)
    }

    @Test
    fun `when getLoginScreen fails`() = runTest {
        coEvery { getLoginScreen.invoke(any()) } returns Result.failure(
            Throwable()
        )

        loginViewModel = createViewModel()
        val uiState = loginViewModel.uiState

        Assert.assertEquals(null, uiState.value.screenModel)
        Assert.assertEquals(SERVER_ERROR_TITLE, uiState.value.errorModel?.title)
    }

    @Test
    fun `when call forgotPassword navigationModel should have forgotPassword true`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.forgotPassword()

        Assert.assertEquals(true, loginViewModel.uiState.value.navigationModel?.forgotPassword)
    }

    @Test
    fun `when call login and validations fail`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.value.validateFields)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
    }

    @Test
    fun `when call login and validations succeed authenticate with warning`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()

        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val accessTokenModel = createAccessToken(
            BannerModel(
                icon = "icon",
                title = "title",
                description = "description"
            )
        )

        coEvery { login.invoke(any(), any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.value.validateFields)
        Assert.assertEquals(true, loginViewModel.uiState.value.navigationModel?.isWarning)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
    }

    @Test
    fun `when call login and validations succeed authenticate without warning`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true
        val accessTokenModel = createAccessToken(null)

        coEvery { login.invoke(any(), any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel.login()

        Assert.assertEquals(true, loginViewModel.uiState.value.validateFields)
        Assert.assertEquals(false, loginViewModel.uiState.value.navigationModel?.isWarning)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
    }

    @Test
    fun `when call login and validations succeed authenticate  with error`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.isValidUsername = true
        loginViewModel.isValidPassword = true

        coEvery { login.invoke(any(), any(), any()) } returns Result.failure(Throwable())

        loginViewModel.login()

        Assert.assertEquals(SERVER_ERROR_TITLE, loginViewModel.uiState.value.errorModel?.title)
    }

    @Test
    fun `when call consumeNavigationEvent clear data`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.consumeNavigationEvent()

        Assert.assertEquals(false, loginViewModel.uiState.value.validateFields)
        Assert.assertEquals(null, loginViewModel.uiState.value.navigationModel)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
        Assert.assertEquals("", loginViewModel.password)
        Assert.assertEquals(false, loginViewModel.isValidPassword)
    }

    @Test
    fun `when call showLoginLink uiState should have onLoginLink`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        val loginLink = LoginLink.TERMS_AND_CONDITIONS
        loginViewModel.showLoginLink(loginLink)

        Assert.assertEquals(loginLink, loginViewModel.uiState.value.onLoginLink)
    }

    @Test
    fun `when call consumeLoginLinkEvent uiState should have onLoginLink clear `() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.consumeLoginLinkEvent()

        Assert.assertEquals(null, loginViewModel.uiState.value.onLoginLink)
    }

    @Test
    fun `when call consumeErrorEvent uiState should have errorModel clear`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.consumeErrorEvent()

        Assert.assertEquals(null, loginViewModel.uiState.value.errorModel)
    }

    @Test
    fun `when call consumeWarningEvent uiState should have warning clear`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(
            emptyScreenModel
        )

        loginViewModel = createViewModel()
        loginViewModel.consumeWarningEvent()

        Assert.assertEquals(null, loginViewModel.uiState.value.warning)
    }

    @Test
    fun `when closeActiveSession succeeds the retried login navigates`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        val accessTokenModel = createAccessToken(null)
        coEvery { login.invoke(any(), any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel = createViewModel()
        loginViewModel.closeActiveSession()

        Assert.assertEquals(null, loginViewModel.uiState.value.errorModel)
        Assert.assertNotNull(loginViewModel.uiState.value.navigationModel)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
    }

    @Test
    fun `when closeActiveSession fails it surfaces the error`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        coEvery { login.invoke(any(), any(), any()) } returns Result.failure(Throwable())

        loginViewModel = createViewModel()
        loginViewModel.closeActiveSession()

        Assert.assertEquals(SERVER_ERROR_TITLE, loginViewModel.uiState.value.errorModel?.title)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
    }

    @Test
    fun `when closeActiveSession is called it uses the typed credentials`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        val accessTokenModel = createAccessToken(null)
        coEvery { login.invoke(any(), any(), any()) } returns Result.success(accessTokenModel)

        loginViewModel = createViewModel()
        loginViewModel.username = USERNAME
        loginViewModel.password = PASSWORD
        loginViewModel.closeActiveSession()

        coVerify { login.invoke(USERNAME, PASSWORD, true) }
    }

    @Test
    fun `when onBiometricLogin it requests navigation regardless of crew state`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)

        loginViewModel = createViewModel()
        loginViewModel.onBiometricLogin()

        Assert.assertEquals(true, loginViewModel.uiState.value.navigateToBiometric)
        Assert.assertEquals(null, loginViewModel.uiState.value.errorModel)
        Assert.assertEquals(false, loginViewModel.uiState.value.isLoading)
    }

    @Test
    fun `when arriving with a biometric match it auto-logs in with stored credentials`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)
        coEvery { getLoginCredentials.invoke(USERNAME) } returns
            com.skgtecnologia.sisem.domain.biometric.model.LoginCredentials(USERNAME, PASSWORD)
        coEvery { login.invoke(any(), any(), any()) } returns Result.success(createAccessToken(null))

        val biometricHandle = SavedStateHandle().apply { set("biometricUsername", USERNAME) }
        loginViewModel = LoginViewModel(
            savedStateHandle = biometricHandle,
            androidIdProvider = androidIdProvider,
            getLoginScreen = getLoginScreen,
            login = login,
            faceCredentialStore = faceCredentialStore,
            storeBiometricFromLogin = storeBiometricFromLogin,
            storeLoginCredentials = storeLoginCredentials,
            getLoginCredentials = getLoginCredentials,
            purgeStaleBiometrics = purgeStaleBiometrics
        )

        coVerify { login.invoke(USERNAME, PASSWORD, false) }
        Assert.assertNotNull(loginViewModel.uiState.value.navigationModel)
    }

    @Test
    fun `when consumeBiometricNavigationEvent clears the flag`() = runTest {
        coEvery { getLoginScreen.invoke(ANDROID_ID) } returns Result.success(emptyScreenModel)

        loginViewModel = createViewModel()
        loginViewModel.consumeBiometricNavigationEvent()

        Assert.assertEquals(false, loginViewModel.uiState.value.navigateToBiometric)
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
