package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.deviceauth.usecases.AssociateDevice
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceAuthScreen
import com.valkiria.uicomponents.mocks.getLoginBlockedErrorUiModel
import com.valkiria.uicomponents.mocks.getLoginDuplicatedErrorUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class DeviceAuthViewModel @Inject constructor(
    private val getDeviceAuthScreen: GetDeviceAuthScreen,
    private val associateDevice: AssociateDevice,
    private val androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(DeviceAuthUiState())
        private set

    var vehicleCode by mutableStateOf("")

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getDeviceAuthScreen.invoke(androidIdProvider.getAndroidId())
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
                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = getLoginDuplicatedErrorUiModel() // FIXME: Map ErrorModel
                    )
                }
        }
    }

    fun associateDevice() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            associateDevice.invoke(
                "jarry", // FIXME: Hardcoded data
                androidIdProvider.getAndroidId(),
                "1" // FIXME: Hardcoded data, propagate vehicleCode
            ).onSuccess {
                uiState = uiState.copy(
                    isLoading = false
                )
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")
                uiState = uiState.copy(
                    isLoading = false,
                    errorModel = getLoginBlockedErrorUiModel() // FIXME: Map ErrorModel
                )
            }
        }
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
