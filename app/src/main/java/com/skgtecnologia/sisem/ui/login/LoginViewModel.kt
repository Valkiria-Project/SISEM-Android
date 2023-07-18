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
import com.skgtecnologia.sisem.ui.error.ErrorUiModel
import com.valkiria.uicomponents.props.TextStyle
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
    var password by mutableStateOf("")

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
                    // BACKEND: How to handle errors?
                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = ErrorUiModel(
                            title = "Error",
                            titleTextStyle = TextStyle.BODY_1,
                            text = "Error",
                            textStyle = TextStyle.BODY_1
                        )
                    )
                }
        }
    }

    fun login() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            login.invoke(username, password)
                .onSuccess { loginModel ->
                    Timber.d("Successful login with ${loginModel.username}")
                    uiState = uiState.copy(
                        isLoading = false
                    )
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = ErrorUiModel(
                            title = "Error",
                            titleTextStyle = TextStyle.BODY_1,
                            text = "Error",
                            textStyle = TextStyle.BODY_1
                        )
                    )
                }
        }
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
}
