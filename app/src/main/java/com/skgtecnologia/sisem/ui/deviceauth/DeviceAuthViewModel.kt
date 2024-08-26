package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.DeleteAccessToken
import com.skgtecnologia.sisem.domain.deviceauth.usecases.AssociateDevice
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceAuthScreen
import com.skgtecnologia.sisem.domain.model.banner.disassociateDeviceBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.navigation.LOGIN
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeviceAuthViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val associateDevice: AssociateDevice,
    private val getDeviceAuthScreen: GetDeviceAuthScreen,
    private val deleteAccessToken: DeleteAccessToken
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(DeviceAuthUiState())
        private set

    private var isAssociateDevice: Boolean by mutableStateOf(false)

    var vehicleCode by mutableStateOf("")
    var isValidVehicleCode by mutableStateOf(false)
    var disassociateDeviceState by mutableStateOf(false)

    fun initScreen() {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getDeviceAuthScreen.invoke(androidIdProvider.getAndroidId())
                .onSuccess { deviceAuthScreenModel ->
                    withContext(Dispatchers.Main) {
                        isAssociateDevice = deviceAuthScreenModel.isAssociateDevice()
                        uiState = uiState.copy(
                            screenModel = deviceAuthScreenModel,
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

    private fun ScreenModel.isAssociateDevice() = body.find { it is SegmentedSwitchUiModel } == null

    fun associateDevice(from: String) {
        if (isAssociateDevice) {
            uiState = uiState.copy(validateFields = true)

            if (isValidVehicleCode) {
                associate(from)
            }
        } else {
            associate(from)
        }
    }

    private fun associate(from: String) {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            associateDevice.invoke(
                androidIdProvider.getAndroidId(),
                vehicleCode,
                disassociateDeviceState
            ).onSuccess {
                handleOnSuccess(from)
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
    }

    private suspend fun handleOnSuccess(from: String) {
        if (disassociateDeviceState) {
            withContext(Dispatchers.Main) {
                consumeNavigationEvent()
                uiState = uiState.copy(
                    isLoading = false,
                    disassociateInfoModel = disassociateDeviceBanner().mapToUi()
                )
            }
        } else {
            resetAppState(from)
        }
    }

    private suspend fun resetAppState(from: String) {
        deleteAccessToken.invoke().onSuccess {
            withContext(Dispatchers.Main) {
                uiState = uiState.copy(
                    isLoading = false,
                    navigationModel = DeviceAuthNavigationModel(isCrewList = true, from = from)
                )
            }
        }
    }

    fun cancel(from: String) {
        if (from == LOGIN) {
            uiState = uiState.copy(isLoading = true)

            job?.cancel()
            job = viewModelScope.launch {
                deleteAccessToken.invoke().onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            navigationModel = DeviceAuthNavigationModel(
                                isCancel = true,
                                from = from
                            )
                        )
                    }
                }
            }
        } else {
            uiState = uiState.copy(
                navigationModel = DeviceAuthNavigationModel(isCancel = true, from = from)
            )
        }
    }

    fun cancelBanner(from: String) {
        job?.cancel()
        job = viewModelScope.launch {
            resetAppState(from)
        }
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            validateFields = false,
            navigationModel = null,
            isLoading = false
        )

        resetForm()
    }

    private fun resetForm() {
        vehicleCode = ""
        isValidVehicleCode = false
        disassociateDeviceState = false
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
