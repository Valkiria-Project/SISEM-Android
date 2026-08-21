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
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.authcards.model.AuthCardsIdentifier
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
import com.skgtecnologia.sisem.domain.biometric.usecases.FetchBiometric
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.banner.incompleteCrewBanner
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
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

private const val LOGIN_EMAIL_IDENTIFIER = "LOGIN_EMAIL"
private const val BIOMETRIC_FETCH_TIMEOUT_MS = 5_000L

private val CREW_CARD_IDENTIFIERS = setOf(
    AuthCardsIdentifier.CREW_MEMBER_CARD_DRIVER.name,
    AuthCardsIdentifier.CREW_MEMBER_CARD_DOCTOR.name,
    AuthCardsIdentifier.CREW_MEMBER_CARD_ASSISTANT.name
)

@Suppress("TooManyFunctions", "LongParameterList")
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val androidIdProvider: AndroidIdProvider,
    private val getLoginScreen: GetLoginScreen,
    private val login: Login,
    private val faceCredentialStore: FaceCredentialStore,
    private val fetchBiometric: FetchBiometric,
    private val getAllAccessTokens: GetAllAccessTokens,
    private val getAuthCardsScreen: GetAuthCardsScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
        private set

    private val previousUsername = savedStateHandle.toRoute<AuthRoute.LoginRoute>().username
    private val loggedOutRole = savedStateHandle.toRoute<AuthRoute.LoginRoute>().loggedOutRole

    private var code by mutableStateOf("")
    var username by mutableStateOf("")
    var isValidUsername by mutableStateOf(false)
    var password by mutableStateOf("")
    var isValidPassword by mutableStateOf(false)

    init {
        uiState.update { it.copy(isLoading = true, loggedOutRole = loggedOutRole) }

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
     * Biometric login is a fast re-entry into an already-active crew session on this
     * shared device, so it is only allowed once every expected crew member has signed in
     * with their password. The expected crew size is the number of crew cards returned by
     * the crewList screen (admin button excluded); the signed-in count is the number of
     * stored sessions. If the crew is incomplete the camera is not opened and an
     * exception banner is shown instead.
     *
     * Exception: during a shift change ([loggedOutRole] is set) the crew is intentionally
     * incomplete while one member is replaced, so the gate is skipped and only the vacated
     * role is enforced against the matched face downstream.
     */
    fun onBiometricLogin() {
        // During a shift change (a crew member logged out and is being replaced) the crew
        // is intentionally incomplete, so the crew-completeness gate is skipped. The vacated
        // role is enforced later against the matched face in FaceCameraViewModel.verify().
        if (!loggedOutRole.isNullOrBlank()) {
            uiState.update { it.copy(navigateToBiometric = true) }
            return
        }

        uiState.update { it.copy(isLoading = true) }

        job?.cancel()
        job = viewModelScope.launch {
            val expectedCrew = fetchExpectedCrewSize()
            val signedInCount = getAllAccessTokens.invoke().getOrNull()?.size ?: 0

            if (expectedCrew > 0 && signedInCount < expectedCrew) {
                uiState.update {
                    it.copy(
                        isLoading = false,
                        errorModel = incompleteCrewBanner().mapToUi()
                    )
                }
            } else {
                uiState.update {
                    it.copy(
                        isLoading = false,
                        navigateToBiometric = true
                    )
                }
            }
        }
    }

    private suspend fun fetchExpectedCrewSize(): Int =
        getAuthCardsScreen.invoke(androidIdProvider.getAndroidId())
            .getOrNull()
            ?.body
            ?.count { it.identifier in CREW_CARD_IDENTIFIERS }
            ?: 0

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
                        // Pull any cloud-enrolled embedding (keyed by document) down to local
                        // storage so the biometric login button can appear on the next visit.
                        // Awaited here (not fire-and-forget): navigation clears this ViewModel
                        // right after login, which would cancel a detached coroutine before the
                        // request is even dispatched. Bounded so a slow/unreachable biometric
                        // service never stalls the login.
                        withTimeoutOrNull(BIOMETRIC_FETCH_TIMEOUT_MS) {
                            fetchBiometric(
                                username = accessTokenModel.username,
                                role = accessTokenModel.role,
                                documentNumber = accessTokenModel.document
                            )
                        }
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

    fun consumeSuccessEvent() {
        uiState.update {
            it.copy(
                successBanner = null
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
