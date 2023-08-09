package com.skgtecnologia.sisem.ui.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.login.usecases.GetAllAccessToken
import com.skgtecnologia.sisem.domain.login.usecases.Logout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val logout: Logout,
    private val getAllAccessToken: GetAllAccessToken
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MenuUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getAllAccessToken.invoke()
                .onSuccess { accessTokenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            accessTokenModelList = accessTokenModel
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false
                        // errorModel = throwable.mapToUi()
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
                        // errorModel = throwable.mapToUi()
                    )
                }
        }
    }
}
