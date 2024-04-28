package com.skgtecnologia.sisem.ui.incident

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.incident.usecases.GetIncidentScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
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
class IncidentViewModel @Inject constructor(
    private val getIncidentScreen: GetIncidentScreen,
    private val logoutCurrentUser: LogoutCurrentUser
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(IncidentUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getIncidentScreen.invoke()
                .onSuccess { incidentScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = incidentScreenModel,
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

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = IncidentNavigationModel(back = true)
        )
    }

    fun navigateToStretcherRetention(identifier: String) {
        uiState = uiState.copy(
            navigationModel = IncidentNavigationModel(stretcherRetentionAph = identifier)
        )
    }

    fun navigateToAphView(patient: String) {
        uiState = uiState.copy(
            navigationModel = IncidentNavigationModel(
                patientAph = patient
            )
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
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
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
