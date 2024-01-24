package com.skgtecnologia.sisem.ui.inventory.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.deviceauth.usecases.GetDeviceType
import com.skgtecnologia.sisem.domain.inventory.model.InventoryType
import com.skgtecnologia.sisem.domain.inventory.usecases.GetInventoryViewScreen
import com.skgtecnologia.sisem.domain.inventory.usecases.SaveTransferReturn
import com.skgtecnologia.sisem.domain.model.banner.confirmTransferReturn
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.INVENTORY_TYPE
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val CANT_EXISTS_KEY = "KEY_CANT_EXISTS"
private const val VEHICLE_CODE_KEY = "KEY_VEHICLE_CODE"
private const val VEHICLE_TYPE_KEY = "KEY_VEHICLE_TYPE"
private const val CODE_LIMIT = 4
private const val DEVICE_TYPE_TEXT = "<font color=\"#FFFFFF\"><b>%s</b></font>"

@Suppress("TooManyFunctions")
@HiltViewModel
class InventoryViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInventoryViewScreen: GetInventoryViewScreen,
    private val getDeviceType: GetDeviceType,
    private val saveTransferReturn: SaveTransferReturn,
    private val logoutCurrentUser: LogoutCurrentUser,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(InventoryViewUiState())
        private set

    private val inventoryName: String? = savedStateHandle[INVENTORY_TYPE]

    private var chipSelectionValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    private var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    private var dropDownValue = mutableStateMapOf<String, DropDownInputUiModel>()
    private var labelValue = mutableStateMapOf<String, String>()

    init {
        val inventoryType = InventoryType.from(inventoryName.orEmpty())

        if (inventoryType != null) {
            uiState = uiState.copy(isLoading = true)

            job?.cancel()
            job = viewModelScope.launch {
                getInventoryViewScreen.invoke(
                    inventoryType = inventoryType,
                    serial = androidIdProvider.getAndroidId()
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

    fun handleChipSelectionAction(chipSelectionAction: GenericUiAction.ChipSelectionAction) {
        chipSelectionValues[chipSelectionAction.identifier] =
            chipSelectionAction.chipSelectionItemUiModel

        var updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = chipSelectionAction.identifier,
            updater = { model ->
                if (model is ChipSelectionUiModel) {
                    model.copy(selected = chipSelectionAction.chipSelectionItemUiModel.name)
                } else {
                    model
                }
            }
        )

        chipSelectionAction.viewsVisibility.forEach { viewsVisibility ->
            updateBodyModel(
                uiModels = updatedBody,
                identifier = viewsVisibility.key
            ) { model ->
                updateComponentVisibility(model, viewsVisibility)
            }.also { body -> updatedBody = body }
        }

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun handleDropDownAction(dropDownAction: GenericUiAction.DropDownAction) {
        dropDownValue[dropDownAction.identifier] = DropDownInputUiModel(
            dropDownAction.identifier,
            dropDownAction.id,
            dropDownAction.name,
            dropDownAction.quantity,
            dropDownAction.fieldValidated
        )

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = CANT_EXISTS_KEY,
            updater = { model ->
                if (model is LabelUiModel) {
                    labelValue[CANT_EXISTS_KEY] = dropDownAction.quantity.toString()
                    model.copy(text = dropDownAction.quantity.toString())
                } else {
                    model
                }
            }
        )

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun handleInputAction(inputAction: GenericUiAction.InputAction) {
        fieldsValues[inputAction.identifier] = InputUiModel(
            inputAction.identifier,
            inputAction.updatedValue,
            inputAction.fieldValidated,
            inputAction.required
        )

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = inputAction.identifier,
            updater = { model ->
                if (model is TextFieldUiModel) {
                    model.copy(text = inputAction.updatedValue)
                } else {
                    model
                }
            }
        )

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )

        if (
            inputAction.identifier == VEHICLE_CODE_KEY &&
            inputAction.updatedValue.length == CODE_LIMIT
        ) {
            getDeviceType(inputAction.updatedValue)
        }
    }

    private fun getDeviceType(code: String) {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getDeviceType.invoke(code).onSuccess { deviceModel ->
                withContext(Dispatchers.Main) {
                    val updatedBody = updateBodyModel(
                        uiModels = uiState.screenModel?.body,
                        identifier = VEHICLE_TYPE_KEY,
                        updater = { model ->
                            if (model is RichLabelUiModel) {
                                model.copy(
                                    text = String.format(DEVICE_TYPE_TEXT, deviceModel.message)
                                )
                            } else {
                                model
                            }
                        }
                    )

                    uiState = uiState.copy(
                        isLoading = false,
                        screenModel = uiState.screenModel?.copy(
                            body = updatedBody
                        )
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

    private fun updateComponentVisibility(
        model: BodyRowModel,
        viewsVisibility: Map.Entry<String, Boolean>
    ) = when (model) {
        is DetailedInfoListUiModel -> model.copy(visibility = viewsVisibility.value)

        is DropDownUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                dropDownValue.remove(viewsVisibility.key)
                model.copy(
                    selected = "",
                    visibility = viewsVisibility.value
                )
            }
        }

        is LabelUiModel -> model.copy(visibility = viewsVisibility.value)

        is RichLabelUiModel -> model.copy(visibility = viewsVisibility.value)

        is TextFieldUiModel -> {
            if (viewsVisibility.value) {
                fieldsValues[viewsVisibility.key] = InputUiModel(
                    identifier = viewsVisibility.key,
                    updatedValue = ""
                )
                model.copy(visibility = viewsVisibility.value)
            } else {
                fieldsValues.remove(viewsVisibility.key)
                model.copy(
                    text = "",
                    visibility = viewsVisibility.value
                )
            }
        }

        else -> model
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

        val isValidDropDown = dropDownValue
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        val areValidChip = chipSelectionValues.size == 2

        if (areValidFields && isValidDropDown && areValidChip) {
            uiState = uiState.copy(
                infoEvent = confirmTransferReturn().mapToUi()
            )
        }
    }

    fun saveTransferReturn() {
        uiState = uiState.copy(
            infoEvent = null,
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch {
            saveTransferReturn.invoke(
                fieldsValues = fieldsValues.mapValues { it.value.updatedValue },
                dropDownValues = dropDownValue.mapValues { it.value.id },
                chipSelectionValues = chipSelectionValues.mapValues { it.value.id },
                labelValues = labelValue
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
