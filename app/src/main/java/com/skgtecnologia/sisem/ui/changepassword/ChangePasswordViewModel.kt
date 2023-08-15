package com.skgtecnologia.sisem.ui.changepassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.changepassword.usecases.ChangePassword
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetChangePasswordScreen
import com.skgtecnologia.sisem.domain.model.error.changePasswordEmptyFields
import com.skgtecnologia.sisem.domain.model.error.changePasswordNoMatch
import com.skgtecnologia.sisem.domain.model.error.mapToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val getChangePasswordScreen: GetChangePasswordScreen,
    private val changePassword: ChangePassword
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(ChangePasswordUiState())
        private set

    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var isValidNewPassword by mutableStateOf(false)
    var confirmNewPassword by mutableStateOf("")
    var isValidConfirmNewPassword by mutableStateOf(false)

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getChangePasswordScreen.invoke()
                .onSuccess {
                    uiState = uiState.copy(
                        isLoading = false,
                        screenModel = it
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

    fun change() {
        uiState = uiState.copy(
            validateFields = true
        )

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            uiState = uiState.copy(
                errorModel = changePasswordEmptyFields().mapToUi()
            )

            return
        }

        if (newPassword != confirmNewPassword) {
            uiState = uiState.copy(
                errorModel = changePasswordNoMatch().mapToUi()
            )

            return
        }

        if (isValidNewPassword && isValidConfirmNewPassword) {
            changePassword()
        }
    }

    private fun changePassword() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            changePassword.invoke(oldPassword = oldPassword, newPassword = newPassword)
                .onSuccess {
                    uiState = uiState.copy(
                        isLoading = false,
                        onChangePassword = true
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

    fun onChangePasswordHandled() {
        uiState = uiState.copy(
            onChangePassword = false,
            validateFields = false,
            isLoading = false
        )

        resetForm()
    }

    private fun resetForm() {
        oldPassword = ""
        newPassword = ""
        confirmNewPassword = ""
        isValidNewPassword = false
        isValidConfirmNewPassword = false
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
