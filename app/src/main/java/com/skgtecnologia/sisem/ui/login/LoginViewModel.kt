package com.skgtecnologia.sisem.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.Login
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.model.error.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.StoreAmbulanceCode
import com.skgtecnologia.sisem.ui.navigation.LoginNavigationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginScreen: GetLoginScreen,
    private val login: Login,
    private val storeAmbulanceCode: StoreAmbulanceCode,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(LoginUiState())
        private set

    var code by mutableStateOf("")
        private set

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
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = loginScreenModel.copy(
                                body = loginScreenModel.body
                            ),
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

    fun storeAmbulanceCode(ambulanceCode: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            storeAmbulanceCode.invoke(ambulanceCode).onSuccess {
                code = ambulanceCode
            }
        }
    }

    fun login() {
        uiState = uiState.copy(
            validateFields = true
        )

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
                                isAdmin = this.isAdmin,
                                isTurnComplete = this.turn?.isComplete == true,
                                requiresPreOperational = this.preoperational?.status == true,
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
