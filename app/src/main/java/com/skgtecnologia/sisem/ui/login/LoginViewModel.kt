package com.skgtecnologia.sisem.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.ChipModel
import com.skgtecnologia.sisem.domain.model.error.mapToUi
import com.skgtecnologia.sisem.ui.navigation.LoginNavigationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginScreen: GetLoginScreen,
    private val login: Login,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(LoginUiState())
        private set

    private var code by mutableStateOf("")
    var username by mutableStateOf("")
    var isValidUsername by mutableStateOf(false)
    var password by mutableStateOf("")
    var isValidPassword by mutableStateOf(false)

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getLoginScreen.invoke(androidIdProvider.getAndroidId())
                .onSuccess { loginScreenModel ->
                    loginScreenModel.body.toVehicleCode()
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = loginScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
        }
    }

    private fun List<BodyRowModel>.toVehicleCode() {
        code = (this.find { it is ChipModel && it.text.isNotBlank() } as? ChipModel)?.text.orEmpty()
    }

    fun login() {
        uiState = uiState.copy(
            validateFields = true
        )

        // FIXME: Move this to the Use Case
        if (isValidUsername && isValidPassword) {
            authenticate()
        }
    }

    private fun authenticate() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            login.invoke(username, password)
                .onSuccess { accessTokenModel ->
                    Timber.d("Successful login with ${accessTokenModel.username}")
                    uiState = uiState.copy(
                        onLogin = true,
                        loginNavigationModel = with(accessTokenModel) {
                            LoginNavigationModel(
                                isAdmin = isAdmin,
                                isTurnComplete = turn?.isComplete == true,
                                requiresPreOperational = preoperational?.status == true,
                                preOperationRole = OperationRole.getRoleByName(role),
                                requiresDeviceAuth = code.isEmpty()
                            )
                        }
                    )
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
        }
    }

    fun onLoginHandled() {
        uiState = uiState.copy(
            onLogin = false,
            validateFields = false,
            loginNavigationModel = null,
            isLoading = false
        )

        resetForm()
    }

    private fun resetForm() {
        username = ""
        isValidUsername = false
        password = ""
        isValidPassword = false
    }

    fun showBottomSheet(link: LoginLink) {
        uiState = uiState.copy(
            onLoginLink = link
        )
    }

    fun handleShownBottomSheet() {
        uiState = uiState.copy(
            onLoginLink = null
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
