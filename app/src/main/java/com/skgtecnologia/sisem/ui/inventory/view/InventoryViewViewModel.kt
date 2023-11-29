package com.skgtecnologia.sisem.ui.inventory.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.inventory.usecases.GetInventoryViewScreen
import com.skgtecnologia.sisem.domain.inventory.usecases.SaveTransferReturn
import com.skgtecnologia.sisem.domain.model.banner.confirmTransferReturn
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.INVENTORY_TYPE
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InventoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInventoryViewScreen: GetInventoryViewScreen,
    private val saveTransferReturn: SaveTransferReturn,
    private val logoutCurrentUser: LogoutCurrentUser
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(InventoryViewUiState())
        private set

    private val inventoryName: String? = savedStateHandle[INVENTORY_TYPE]

    var chipValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    var dropDownValue = mutableStateOf(DropDownInputUiModel())

    init {
        val inventoryType = InventoryType.from(inventoryName.orEmpty())

        if (inventoryType != null) {
            uiState = uiState.copy(isLoading = true)

            job?.cancel()
            job = viewModelScope.launch {
                getInventoryViewScreen.invoke(
                    inventoryType = inventoryType
                ).onSuccess { inventoryScreenModel ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = inventoryScreenModel,
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
        } else {
            goBack()
        }
    }

    fun save() {
        uiState = uiState.copy(
            validateFields = true
        )

        val areValidFields = fieldsValues
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        val isValidDropDown = dropDownValue.value.fieldValidated

        val areValidChip = chipValues.size == 2

        if (areValidFields && isValidDropDown && areValidChip) {
            uiState = uiState.copy(
                infoEvent = confirmTransferReturn().mapToUi()
            )
        }
    }

    fun saveTransferReturn() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch {
            saveTransferReturn.invoke(
                fieldsValues = fieldsValues.mapValues { it.value.updatedValue }
            ).onSuccess {
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        navigationModel = InventoryViewNavigationModel(back = true)
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
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = InventoryViewNavigationModel(back = true)
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            infoEvent = null,
            isLoading = false
        )
    }

    fun consumeInfoEvent() {
        uiState = uiState.copy(
            infoEvent = null
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
