package com.skgtecnologia.sisem.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.LogoutTurn
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getAllAccessTokens: GetAllAccessTokens,
    private val logout: Logout,
    private val logoutTurn: LogoutTurn,
    private val observeOperationConfig: ObserveOperationConfig
) : ViewModel() {

    private var job: Job? = null

    var uiState: MutableStateFlow<MenuUiState> = MutableStateFlow(MenuUiState())
        private set

    init {
        uiState.update { it.copy(isLoading = true) }

        job?.cancel()
        job = viewModelScope.launch {
            getAllAccessTokens.invoke()
                .onSuccess { accessTokenModel ->
                    retrieveOperationConfig(accessTokenModel)
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

    private suspend fun retrieveOperationConfig(accessTokenModel: List<AccessTokenModel>) {
        observeOperationConfig.invoke()
            .onSuccess { operationModel ->
                withContext(Dispatchers.Main) {
                    uiState.update {
                        it.copy(
                            vehicleConfig = operationModel.vehicleConfig,
                            accessTokenModelList = accessTokenModel,
                            isLoading = false
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

    fun consumeErrorEvent() {
        uiState.update {
            it.copy(
                errorModel = null
            )
        }
    }
}
