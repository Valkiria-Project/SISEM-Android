package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceAuthScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class DeviceAuthViewModel @Inject constructor(
    private val getDeviceAuthScreen: GetDeviceAuthScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(DeviceAuthUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getDeviceAuthScreen.invoke("123") // FIXME: Do same as LoginViewModel
                .onSuccess { deviceAuthScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = deviceAuthScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                }
        }
    }
}
