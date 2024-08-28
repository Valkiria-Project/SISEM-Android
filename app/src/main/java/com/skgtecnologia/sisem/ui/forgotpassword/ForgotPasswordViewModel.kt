package com.skgtecnologia.sisem.ui.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.forgotpassword.usecases.GetForgotPasswordScreen
import com.skgtecnologia.sisem.domain.forgotpassword.usecases.SendEmail
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.sendEmailSuccessBanner
import com.valkiria.uicomponents.components.textfield.InputUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val getForgotPasswordScreen: GetForgotPasswordScreen,
    private val sendEmail: SendEmail
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(ForgotPasswordUiState())
        private set

    var emailValue = mutableStateOf(InputUiModel("", "", false))

    fun initScreen() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getForgotPasswordScreen.invoke()
                .onSuccess { forgotPasswordScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = forgotPasswordScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorModel = throwable.mapToUi()
                        )
                    }
                }
        }
    }

    fun sendEmail() {
        uiState = uiState.copy(
            validateFields = true
        )

        if (emailValue.value.fieldValidated) {
            uiState = uiState.copy(
                isLoading = true
            )

            job?.cancel()
            job = viewModelScope.launch {
                sendEmail.invoke(emailValue.value.updatedValue)
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                successBanner = sendEmailSuccessBanner().mapToUi(),
                                isLoading = false
                            )
                        }
                    }
                    .onFailure { throwable ->
                        Timber.wtf(throwable, "This is a failure")
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
            }
        }
    }

    fun cancel() {
        uiState = uiState.copy(
            navigationModel = ForgotPasswordNavigationModel(
                isCancel = true
            )
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            validateFields = false,
            navigationModel = null,
            successBanner = null,
            isLoading = false
        )
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }

    fun consumeSuccessEvent() {
        uiState = uiState.copy(
            successBanner = null,
            navigationModel = ForgotPasswordNavigationModel(
                isSuccess = true
            )
        )
    }
}
