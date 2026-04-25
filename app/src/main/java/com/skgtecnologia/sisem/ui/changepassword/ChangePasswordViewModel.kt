package com.skgtecnologia.sisem.ui.changepassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.changepassword.usecases.ChangePassword
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetChangePasswordScreen
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.changepassword.usecases.OnCancel
import com.skgtecnologia.sisem.domain.model.banner.changePasswordEmptyFieldsBanner
import com.skgtecnologia.sisem.domain.model.banner.changePasswordNoMatchBanner
import com.skgtecnologia.sisem.domain.model.banner.changePasswordSuccessBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val getChangePasswordScreen: GetChangePasswordScreen,
    private val getLoginNavigationModel: GetLoginNavigationModel,
    private val onCancel: OnCancel,
    private val changePassword: ChangePassword
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(ChangePasswordUiState())
        private set

    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var isValidNewPassword by mutableStateOf(false)
    var confirmedNewPassword by mutableStateOf("")
    var isValidConfirmedNewPassword by mutableStateOf(false)

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getChangePasswordScreen.invoke()
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            screenModel = it
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

    fun change() {
        // TECH-DEBT: Move this to the Use Case
        uiState = uiState.copy(
            validateFields = true
        )

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmedNewPassword.isEmpty()) {
            uiState = uiState.copy(
                errorModel = changePasswordEmptyFieldsBanner().mapToUi()
            )

            return
        }

        if (newPassword != confirmedNewPassword) {
            uiState = uiState.copy(
                errorModel = changePasswordNoMatchBanner().mapToUi()
            )

            return
        }

        if (isValidNewPassword && isValidConfirmedNewPassword) {
            changePassword()
        }
    }

    private fun changePassword() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch {
            changePassword.invoke(oldPassword = oldPassword, newPassword = newPassword)
                .onSuccess {
                    getLoginNavigationModel()
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

    private suspend fun getLoginNavigationModel() {
        getLoginNavigationModel.invoke()
            .onSuccess { loginNavigationModel ->
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        successInfoModel = changePasswordSuccessBanner().mapToUi(),
                        loginNavigationModel = loginNavigationModel,
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

    fun consumeChangePasswordEvent() {
        uiState = uiState.copy(
            validateFields = false,
            successInfoModel = null,
            isLoading = false
        )

        resetForm()
    }

    private fun resetForm() {
        oldPassword = ""
        newPassword = ""
        isValidNewPassword = false
        confirmedNewPassword = ""
        isValidConfirmedNewPassword = false
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }

    fun cancel() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch {
            onCancel.invoke()
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            onCancel = true
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

    fun consumeCancelEvent() {
        uiState = uiState.copy(
            onCancel = false
        )
    }
}
