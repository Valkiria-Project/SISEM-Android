package com.skgtecnologia.sisem.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.commons.biometric.FaceCredentialStore
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.biometric.usecases.GetLoginCredentials
import com.skgtecnologia.sisem.domain.biometric.usecases.PurgeStaleBiometrics
import com.skgtecnologia.sisem.domain.biometric.usecases.StoreBiometricFromLogin
import com.skgtecnologia.sisem.domain.biometric.usecases.StoreLoginCredentials
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val LOGIN_EMAIL_IDENTIFIER = "LOGIN_EMAIL"

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val androidIdProvider: AndroidIdProvider,
    private val getLoginScreen: GetLoginScreen,
    private val login: Login,
    private val faceCredentialStore: FaceCredentialStore,
    private val storeBiometricFromLogin: StoreBiometricFromLogin,
    private val storeLoginCredentials: StoreLoginCredentials,
    private val getLoginCredentials: GetLoginCredentials,
    private val purgeStaleBiometrics: PurgeStaleBiometrics
) : ViewModel() {

    private var job: Job? = null

    var uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
        private set

    private val previousUsername = savedStateHandle.toRoute<AuthRoute.LoginRoute>().username
    private val loggedOutRole = savedStateHandle.toRoute<AuthRoute.LoginRoute>().loggedOutRole
    private val biometricUsername = savedStateHandle.toRoute<AuthRoute.LoginRoute>().biometricUsername
    private val targetRole = savedStateHandle.toRoute<AuthRoute.LoginRoute>().targetRole

    private var code by mutableStateOf("")
    var username by mutableStateOf("")
    var isValidUsername by mutableStateOf(false)
    var password by mutableStateOf("")
    var isValidPassword by mutableStateOf(false)

    init {
        uiState.update { it.copy(isLoading = true, loggedOutRole = loggedOutRole, targetRole = targetRole) }

        // Every login-screen load evicts biometric records idle for more than the inactivity
        // window (record + encrypted credentials), independent of the screen fetch.
        viewModelScope.launch { purgeStaleBiometrics() }

        job?.cancel()
        job = viewModelScope.launch {
            getLoginScreen.invoke(androidIdProvider.getAndroidId())
                .onSuccess { loginScreenModel ->
                    loginScreenModel.body.toVehicleCode()
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                screenModel = loginScreenModel.copy(
                                    body = loginScreenModel.body.withPreviousUsername(
                                        previousUsername
                                    )
                                ),
                                isLoading = false
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
                }
        }

        // Arriving from a biometric match: wait for the screen (so the vehicle code is parsed)
        // then silently re-authenticate with the matched user's decrypted credentials.
        biometricUsername?.let { matchedUsername ->
            viewModelScope.launch {
                job?.join()
                autoLoginWithBiometric(matchedUsername)
            }
        }
    }

    private suspend fun autoLoginWithBiometric(matchedUsername: String) {
        val credentials = getLoginCredentials(matchedUsername)
        if (credentials == null) {
            Timber.w("[Biometric] No stored credentials for $matchedUsername; manual login required")
            return
        }
        username = credentials.username
        password = credentials.password
        isValidUsername = true
        isValidPassword = true
        authenticate()
    }

    private fun List<BodyRowModel>.toVehicleCode() {
        code = filterIsInstance<ChipUiModel>()
            .firstOrNull { it.text.isNotBlank() }
            ?.text.orEmpty()
    }

    private fun List<BodyRowModel>.withPreviousUsername(
        previousUsername: String?
    ): List<BodyRowModel> {
        return if (previousUsername.isNullOrBlank()) {
            this
        } else {
            updateBodyModel(
                uiModels = this,
                identifier = LOGIN_EMAIL_IDENTIFIER,
                updater = { model ->
                    if (model is TextFieldUiModel) {
                        username = previousUsername
                        isValidUsername = true

                        model.copy(text = previousUsername)
                    } else {
                        model
                    }
                }
            )
        }
    }

    /**
     * Biometric login is a shortcut that runs the normal login with the matched user's stored
     * credentials. The matched face resolves to a user, the login executes, and routing lands
     * on that user's cards like a manual login. [targetRole] (the tapped card, or the vacated
     * role during a shift change) travels down to FaceCameraViewModel.verify(), which blocks
     * the match — without ever reaching here — if that user already has an active session
     * under a different role.
     */
    fun onBiometricLogin() {
        uiState.update { it.copy(navigateToBiometric = true) }
    }

    fun consumeBiometricNavigationEvent() {
        uiState.update { it.copy(navigateToBiometric = false) }
    }

    fun forgotPassword() {
        uiState.update {
            it.copy(
                navigationModel = LoginNavigationModel(forgotPassword = true)
            )
        }
    }

    fun login() {
        uiState.update {
            it.copy(
                validateFields = true
            )
        }

        // TECH-DEBT: Move this to the Use Case
        if (isValidUsername && isValidPassword) {
            authenticate()
        }
    }

    @Suppress("LongMethod")
    private fun authenticate(forceCloseSession: Boolean = false) {
        uiState.update {
            it.copy(
                isLoading = true
            )
        }

        job?.cancel()
        job = viewModelScope.launch {
            login.invoke(username, password, forceCloseSession)
                .onSuccess { accessTokenModel ->
                    Timber.d("Successful login with ${accessTokenModel.username}")
                    if (accessTokenModel.warning == null) {
                        // Persist credentials needed for face authentication
                        faceCredentialStore.storeRefreshToken(
                            accessTokenModel.username,
                            accessTokenModel.refreshToken
                        )
                        faceCredentialStore.storeRole(
                            accessTokenModel.username,
                            accessTokenModel.role
                        )
                        // The auth/login response now carries the enrolled embeddings, so they
                        // are persisted locally straight from it (no separate fetch call). This
                        // lets the biometric login button appear on the next visit.
                        storeBiometricFromLogin(
                            username = accessTokenModel.username,
                            role = accessTokenModel.role,
                            documentNumber = accessTokenModel.document,
                            embeddings = accessTokenModel.embeddings
                        )
                        // Persist the password encrypted so a later biometric match can silently
                        // re-authenticate; this also refreshes the inactivity window.
                        storeLoginCredentials(accessTokenModel.username, password)
                        val navModel = with(accessTokenModel) {
                            LoginNavigationModel(
                                isAdmin = isAdmin,
                                isTurnComplete = turn?.isComplete == true,
                                requiresPreOperational =
                                preoperational?.status == true && configPreoperational,
                                preOperationRole = OperationRole.getRoleByName(role),
                                requiresDeviceAuth = code.isEmpty()
                            )
                        }
                        uiState.update {
                            it.copy(
                                navigationModel = navModel,
                                isLoading = false
                            )
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            uiState.update {
                                it.copy(
                                    warning = accessTokenModel.warning.mapToUi(),
                                    navigationModel = with(accessTokenModel) {
                                        LoginNavigationModel(
                                            isWarning = true,
                                            isAdmin = isAdmin,
                                            isTurnComplete = turn?.isComplete == true,
                                            requiresPreOperational =
                                            preoperational?.status == true && configPreoperational,
                                            preOperationRole = OperationRole.getRoleByName(role),
                                            requiresDeviceAuth = code.isEmpty()
                                        )
                                    },
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
                }
        }
    }

    fun consumeNavigationEvent() {
        uiState.update {
            it.copy(
                validateFields = false,
                navigationModel = null,
                isLoading = false
            )
        }

        resetForm()
    }

    private fun resetForm() {
        password = ""
        isValidPassword = false
    }

    fun showLoginLink(link: LoginLink) {
        uiState.update {
            it.copy(
                onLoginLink = link
            )
        }
    }

    fun consumeLoginLinkEvent() {
        uiState.update {
            it.copy(
                onLoginLink = null
            )
        }
    }

    fun closeActiveSession() {
        uiState.update { it.copy(errorModel = null) }
        authenticate(forceCloseSession = true)
    }

    fun consumeErrorEvent() {
        uiState.update {
            it.copy(
                errorModel = null
            )
        }
    }

    fun consumeWarningEvent() {
        uiState.update {
            it.copy(
                warning = null
            )
        }
    }
}
