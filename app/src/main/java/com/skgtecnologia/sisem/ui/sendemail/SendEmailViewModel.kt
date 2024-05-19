package com.skgtecnologia.sisem.ui.sendemail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.sendemail.usecases.SendEmail
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.sendEmailScreenSuccessBanner
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.textfield.InputUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SendEmailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sendEmail: SendEmail,
    private val logoutCurrentUser: LogoutCurrentUser
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(SendEmailUiState())
        private set

    private val idAph: Int? = savedStateHandle[NavigationArgument.ID_APH]

    var emailValue = mutableStateOf(InputUiModel("", "", false))
    var bodyValue = mutableStateOf("")

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
                sendEmail.invoke(
                    to = emailValue.value.updatedValue,
                    body = bodyValue.value,
                    idAph = idAph.toString()
                ).onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            infoEvent = sendEmailScreenSuccessBanner().mapToUi()
                        )
                    }
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorEvent = it.mapToUi()
                        )
                    }
                }
            }
        }
    }

    fun cancel() {
        uiState = uiState.copy(
            navigationModel = SendEmailNavigationModel(back = true)
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            validateFields = false,
            navigationModel = null,
            infoEvent = null,
            isLoading = false,
        )
    }

    fun handleEvent(uiAction: UiAction) {
        consumeShownError()

        uiAction.handleAuthorizationErrorEvent {
            job?.cancel()
            job = viewModelScope.launch {
                logoutCurrentUser.invoke()
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
                    }.onFailure {
                        UnauthorizedEventHandler.publishUnauthorizedEvent(it.toString())
                    }
            }
        }
    }

    private fun consumeShownError() {
        uiState = uiState.copy(
            errorEvent = null
        )
    }

    fun navigateToMain() {
        uiState = uiState.copy(
            navigationModel = SendEmailNavigationModel(send = true)
        )
    }
}
