package com.skgtecnologia.sisem.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.LogoutTurn
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.ui.commons.extensions.STATE_FLOW_STARTED_TIME
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.valkiria.uicomponents.action.UiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getAllAccessTokens: GetAllAccessTokens,
    private val logout: Logout,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val logoutTurn: LogoutTurn,
    observeOperationConfig: ObserveOperationConfig
) : ViewModel() {

    private var job: Job? = null

    var uiState: MutableStateFlow<MenuUiState> = MutableStateFlow(MenuUiState())
        private set

    val operationConfig = observeOperationConfig.invoke()
        .onStart {
            uiState.update { it.copy(isLoading = true) }
            getAllAccessTokens.invoke()
                .onSuccess { accessTokenModels ->
                    Timber.d("Success getting the users")

                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                accessTokenModelList = accessTokenModels
                            )
                        }
                    }
                }.onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
                }
        }
        .onEach { operationModel ->
            Timber.d("Update operationModel")

            withContext(Dispatchers.Main) {
                uiState.update {
                    it.copy(
                        vehicleConfig = operationModel.vehicleConfig,
                        isLoading = false
                    )
                }
            }
        }
        .catch { throwable ->
            Timber.wtf(throwable, "This is a failure")

            withContext(Dispatchers.Main) {
                uiState.update {
                    it.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(STATE_FLOW_STARTED_TIME),
            initialValue = null
        )

    fun logout(username: String) {
        uiState.update { it.copy(isLoading = true) }

        job?.cancel()
        job = viewModelScope.launch {
            logoutTurn.invoke(username = username)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                isLogout = true
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
                }
        }
    }

    fun logoutAdmin(username: String) {
        uiState.update { it.copy(isLoading = true) }

        job?.cancel()
        job = viewModelScope.launch {
            logout.invoke(username = username)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                isLogout = true
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    withContext(Dispatchers.Main) {
                        uiState.update {
                            it.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
                }
        }
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
        uiState.update {
            it.copy(
                errorModel = null
            )
        }
    }
}
