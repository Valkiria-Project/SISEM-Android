package com.skgtecnologia.sisem.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.Login
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

@Suppress("TooManyFunctions")
@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val androidIdProvider: AndroidIdProvider,
    private val getLoginScreen: GetLoginScreen,
    private val login: Login
) : ViewModel() {

    private var job: Job? = null

    var uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
        private set

    private val previousUsername = savedStateHandle.toRoute<AuthRoute.LoginRoute>().username

    private var code by mutableStateOf("")
    var username by mutableStateOf("")
    var isValidUsername by mutableStateOf(false)
    var password by mutableStateOf("")
    var isValidPassword by mutableStateOf(false)

    init {
        uiState.update { it.copy(isLoading = true) }

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
    private fun authenticate() {
        uiState.update {
            it.copy(
                isLoading = true
            )
        }

        job?.cancel()
        job = viewModelScope.launch {
            login.invoke(username, password)
                .onSuccess { accessTokenModel ->
                    Timber.d("Successful login with ${accessTokenModel.username}")
                    if (accessTokenModel.warning == null) {
                        uiState.update {
                            it.copy(
                                navigationModel = with(accessTokenModel) {
                                    LoginNavigationModel(
                                        isAdmin = isAdmin,
                                        isTurnComplete = turn?.isComplete == true,
                                        requiresPreOperational =
                                        preoperational?.status == true && configPreoperational,
                                        preOperationRole = OperationRole.getRoleByName(role),
                                        requiresDeviceAuth = code.isEmpty()
                                    )
                                }
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
