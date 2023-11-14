package com.skgtecnologia.sisem.ui.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getAllAccessTokens: GetAllAccessTokens,
    private val observeOperationConfig: ObserveOperationConfig,
    private val logout: Logout
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MenuUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getAllAccessTokens.invoke()
                .onSuccess { accessTokenModel ->
                    retrieveOperationConfig(accessTokenModel)
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

    fun logout(username: String) {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            logout.invoke(username = username)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            isLogout = true
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

    private suspend fun retrieveOperationConfig(accessTokenModel: List<AccessTokenModel>) {
        observeOperationConfig.invoke()
            .onSuccess { operationModel ->
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        vehicleConfig = operationModel.vehicleConfig,
                        accessTokenModelList = accessTokenModel,
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
            }
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
