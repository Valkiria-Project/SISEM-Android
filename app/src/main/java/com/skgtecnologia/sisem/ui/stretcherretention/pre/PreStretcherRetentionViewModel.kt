package com.skgtecnologia.sisem.ui.stretcherretention.pre

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.header.emptyStretcherRetentionHeader
import com.skgtecnologia.sisem.domain.model.label.emptyStretcherRetentionMessage
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.errors.StretchRetentionErrors
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.GetPreStretcherRetentionScreen
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.valkiria.uicomponents.action.UiAction
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

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getPreStretcherRetentionScreen.invoke()
                .onSuccess { stretcherRetentionScreen ->

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = stretcherRetentionScreen,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure ${throwable.localizedMessage}")

                    if (throwable is StretchRetentionErrors.NoIncidentId) {
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                screenModel = ScreenModel(
                                    header = emptyStretcherRetentionHeader(),
                                    body = listOf(emptyStretcherRetentionMessage())
                                ),
                                isLoading = false
                            )
                        }
                    } else {
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

    fun navigateToStretcherView(patient: String) {
        uiState = uiState.copy(
            navigationModel = PreStretcherRetentionNavigationModel(
                patientAph = patient
            )
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            isLoading = false,
            navigationModel = null
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
            infoEvent = null
        )
    }

    fun navigateBack() {
        uiState = uiState.copy(
            navigationModel = PreStretcherRetentionNavigationModel(back = true)
        )
    }
}
