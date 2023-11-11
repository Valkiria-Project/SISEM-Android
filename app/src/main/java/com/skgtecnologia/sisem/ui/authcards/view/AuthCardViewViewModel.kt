package com.skgtecnologia.sisem.ui.authcards.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalViewIdentifier
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetAuthCardViewScreen
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.valkiria.uicomponents.action.UiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class AuthCardViewViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val getAuthCardViewScreen: GetAuthCardViewScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(AuthCardViewUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getAuthCardViewScreen.invoke(androidIdProvider.getAndroidId())
                .onSuccess { preOperationalScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = preOperationalScreenModel,
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

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = AuthCardViewNavigationModel(back = true)
        )
    }

    fun navigate(identifier: String) {
        runCatching {
            uiState = uiState.copy(
                navigationModel = AuthCardViewNavigationModel(role = identifier.identifierToRole())
            )
        }.onFailure {
            uiState = uiState.copy(
                errorModel = it.mapToUi()
            )
        }
    }

    private fun String.identifierToRole(): OperationRole {
        return when (this) {
            PreOperationalViewIdentifier.CREW_MEMBER_CARD_ASSISTANT.name ->
                OperationRole.AUXILIARY_AND_OR_TAPH

            PreOperationalViewIdentifier.CREW_MEMBER_CARD_DRIVER.name ->
                OperationRole.DRIVER

            PreOperationalViewIdentifier.CREW_MEMBER_CARD_DOCTOR.name ->
                OperationRole.MEDIC_APH

            else -> throw IllegalArgumentException("Identifier $this not supported")
        }
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
            errorModel = null
        )
    }
}
