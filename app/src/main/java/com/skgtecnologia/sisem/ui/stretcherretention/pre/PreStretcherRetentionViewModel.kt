package com.skgtecnologia.sisem.ui.stretcherretention.pre

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.GetPreStretcherRetentionScreen
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreStretcherRetentionViewModel @Inject constructor(
    private val logoutCurrentUser: LogoutCurrentUser,
    private val getPreStretcherRetentionScreen: GetPreStretcherRetentionScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreStretcherRetentionUiState())
        private set

    var chipSelectionValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    var fieldsValues = mutableStateMapOf<String, InputUiModel>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getPreStretcherRetentionScreen.invoke()
                .onSuccess { stretcherRetentionScreen ->
                    stretcherRetentionScreen.getFormInitialValues()

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = stretcherRetentionScreen,
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

    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is TextFieldUiModel -> fieldsValues[bodyRowModel.identifier] = InputUiModel(
                    bodyRowModel.identifier,
                    bodyRowModel.text
                )
            }
        }
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            isLoading = false,
            validateFields = false,
            navigationModel = null
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

    fun navigateBack() {
        uiState = uiState.copy(
            successEvent = null,
            navigationModel = PreStretcherRetentionNavigationModel(back = true)
        )
    }
}
