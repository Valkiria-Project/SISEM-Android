package com.skgtecnologia.sisem.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.usecases.GetLoginScreen
import com.skgtecnologia.sisem.domain.login.usecases.Login
import com.skgtecnologia.sisem.domain.model.error.mapToUi
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
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(LoginUiState())
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

    fun login() {
        uiState = uiState.copy(
            validateFields = true
        )
    }

    fun authenticate() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            login.invoke(username, password)
                .onSuccess { accessTokenModel ->
                    Timber.d("Successful login with ${accessTokenModel.username}")
                    uiState = uiState.copy(
                        onLogin = true
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

    fun onFieldsValidated() {
        uiState = uiState.copy(
            validateFields = false
        )
    }

    fun onLoginHandled() {
        uiState = uiState.copy(
            onLogin = false,
            isLoading = false
        )
    }

    fun showBottomSheet(link: LoginLink) {
        uiState = uiState.copy(
            bottomSheetLink = link
        )
    }

    fun handleShownBottomSheet() {
        uiState = uiState.copy(
            bottomSheetLink = null
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
