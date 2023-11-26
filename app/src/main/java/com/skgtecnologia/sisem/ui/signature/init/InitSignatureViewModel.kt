package com.skgtecnologia.sisem.ui.signature.init

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.signature.usecases.GetInitSignatureScreen
import com.skgtecnologia.sisem.domain.signature.usecases.SearchDocument
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.textfield.InputUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InitSignatureViewModel @Inject constructor(
    private val getInitSignatureScreen: GetInitSignatureScreen,
    private val searchDocument: SearchDocument,
    private val logoutCurrentUser: LogoutCurrentUser
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(InitSignatureUiState())
        private set

    var document = mutableStateOf(InputUiModel("", ""))

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getInitSignatureScreen.invoke()
                .onSuccess { initSignatureScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = initSignatureScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            infoEvent = throwable.mapToUi()
                        )
                    }
                }
        }
    }

    fun searchDocument() {
        uiState = uiState.copy(
            validateFields = true
        )

        if (document.value.fieldValidated) {
            uiState = uiState.copy(isLoading = true)

            job?.cancel()
            job = viewModelScope.launch {
                searchDocument.invoke(document.value.updatedValue)
                    .onSuccess {
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                navigationModel = InitSignatureNavigationModel(
                                    document = document.value.updatedValue
                                )
                            )
                        }
                    }
                    .onFailure { throwable ->
                        Timber.wtf(throwable, "This is a failure")

                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                infoEvent = throwable.mapToUi()
                            )
                        }
                    }
            }
        }
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = InitSignatureNavigationModel(back = true)
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun handleEvent(uiAction: UiAction) {
        consumeShownError()

        uiAction.handleAuthorizationErrorEvent {
            job?.cancel()
            job = viewModelScope.launch {
                logoutCurrentUser.invoke()
                    .onSuccess {
                        UnauthorizedEventHandler.publishUnauthorizedEvent()
                    }
            }
        }
    }

    private fun consumeShownError() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }
}
