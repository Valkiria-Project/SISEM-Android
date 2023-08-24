package com.skgtecnologia.sisem.ui.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.auth.usecases.Logout
import com.skgtecnologia.sisem.domain.operation.usecases.RetrieveOperationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getAllAccessTokens: GetAllAccessTokens,
    private val retrieveOperationConfig: RetrieveOperationConfig,
    private val logout: Logout
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MenuUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getAllAccessTokens.invoke()
                .onSuccess { accessTokenModel ->
                    retrieveOperationConfig(accessTokenModel)
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false
                        // errorModel = throwable.mapToUi() // FIXME
                    )
                }
        }
    }

    fun logout(username: String) {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
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

                    uiState = uiState.copy(
                        isLoading = false
                        // errorModel = throwable.mapToUi() // FIXME
                    )
                }
        }
    }

    private suspend fun retrieveOperationConfig(accessTokenModel: List<AccessTokenModel>) {
        retrieveOperationConfig.invoke()
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

                uiState = uiState.copy(
                    isLoading = false
                    // errorModel = throwable.mapToUi() // FIXME
                )
            }
    }
}
