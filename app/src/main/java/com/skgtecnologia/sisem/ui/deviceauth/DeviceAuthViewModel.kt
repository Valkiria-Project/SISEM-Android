package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.DeleteAccessToken
import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import com.skgtecnologia.sisem.domain.deviceauth.usecases.AssociateDevice
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceAuthScreen
import com.skgtecnologia.sisem.domain.model.error.mapToUi
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
    private val deleteAccessToken: DeleteAccessToken,
    private val androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(DeviceAuthUiState())
        private set

    var vehicleCode by mutableStateOf("") // FIXME
    var disassociateDeviceState by mutableStateOf(false)
    var isValidVehicleCode by mutableStateOf(false)

    init {
        getScreen()
    }

    private fun getScreen() {
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
                        errorModel = throwable.mapToUi()
                    )
                }
        }
    }

    fun associateDevice() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            associateDevice.invoke(
                androidIdProvider.getAndroidId(),
                vehicleCode,
                disassociateDeviceState
            ).onSuccess { associateDeviceModel ->
                handleOnSuccess(associateDeviceModel)
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    errorModel = throwable.mapToUi()
                )
            }
        }
    }

    private fun handleOnSuccess(associateDeviceModel: AssociateDeviceModel) {
        if (disassociateDeviceState) {
            getScreen() // FIXME: show message?
        } else {
            job?.cancel()
            job = viewModelScope.launch(Dispatchers.IO) {
                deleteAccessToken.invoke().onSuccess {
                    uiState = uiState.copy(
                        isLoading = false,
                        onDeviceAuthenticated = true
                    )
                }
            }
        }
    }

    fun onDeviceAuthHandled() {
        uiState = uiState.copy(
            onDeviceAuthenticated = false,
            validateFields = false,
            isLoading = false
        )

        resetForm()
    }

    private fun resetForm() {
        vehicleCode = ""
        isValidVehicleCode = false
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
