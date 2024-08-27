package com.skgtecnologia.sisem.ui.inventory.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

const val ELEMENT_TYPE_KEY = "KEY_ELEMENT_TYPE"
const val MOV_TYPE_KEY = "KEY_MOV_TYPE"

private const val CANT_EXISTS_KEY = "KEY_CANT_EXISTS"
private const val GENERIC_INS_NAME_KEY = "KEY_GENERIC_INS_NAME"
private const val GENERIC_MED_NAME_KEY = "KEY_GENERIC_MED_NAME"
private const val CANT_TRANSFER_KEY = "KEY_CANT_TRANSFER"
private const val VEHICLE_CODE_KEY = "KEY_VEHICLE_CODE"
private const val VEHICLE_TYPE_KEY = "KEY_VEHICLE_TYPE"
private const val CANT_RETURN_KEY = "KEY_CANT_RETURN"
private const val SUPPLIES = "INSUMO"
private const val MEDICATION = "MEDICATION"
private const val TRANSFER = "TRANSFER"
private const val RETURN = "RETURN"
private const val CODE_LIMIT = 4
private const val DEVICE_TYPE_TEXT = "<font color=\"#FFFFFF\"><b>%s</b></font>"

@Suppress("TooManyFunctions")
@HiltViewModel
class InventoryViewViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val getInventoryViewScreen: GetInventoryViewScreen,
    private val getDeviceType: GetDeviceType,
    private val saveTransferReturn: SaveTransferReturn,
    private val logoutCurrentUser: LogoutCurrentUser
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(InventoryViewUiState())
        private set

    private var chipSelectionValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    private var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    private var dropDownValue = mutableStateMapOf<String, DropDownInputUiModel>()
    private var labelValue = mutableStateMapOf<String, String>()

    private val suppliesMap = mapOf(
        GENERIC_INS_NAME_KEY to true,
        GENERIC_MED_NAME_KEY to false,
        CANT_EXISTS_KEY to true
    )

    private val medMap = mapOf(
        GENERIC_INS_NAME_KEY to false,
        GENERIC_MED_NAME_KEY to true,
        CANT_EXISTS_KEY to true
    )

    private val transferMap = mapOf(
        GENERIC_INS_NAME_KEY to false,
        GENERIC_MED_NAME_KEY to false,
        CANT_EXISTS_KEY to true,
        CANT_TRANSFER_KEY to true,
        VEHICLE_CODE_KEY to true,
        VEHICLE_TYPE_KEY to true,
        CANT_RETURN_KEY to false
    )

    private val returnMap = mapOf(
        GENERIC_INS_NAME_KEY to false,
        GENERIC_MED_NAME_KEY to false,
        CANT_EXISTS_KEY to true,
        CANT_TRANSFER_KEY to false,
        VEHICLE_CODE_KEY to false,
        VEHICLE_TYPE_KEY to false,
        CANT_RETURN_KEY to true
    )

    private val transferSuppliesMap = mapOf(
        GENERIC_INS_NAME_KEY to true,
        GENERIC_MED_NAME_KEY to false,
        CANT_EXISTS_KEY to true,
        CANT_TRANSFER_KEY to true,
        VEHICLE_CODE_KEY to true,
        VEHICLE_TYPE_KEY to true,
        CANT_RETURN_KEY to false
    )

    private val transferMedMap = mapOf(
        GENERIC_INS_NAME_KEY to false,
        GENERIC_MED_NAME_KEY to true,
        CANT_EXISTS_KEY to true,
        CANT_TRANSFER_KEY to true,
        VEHICLE_CODE_KEY to true,
        VEHICLE_TYPE_KEY to true,
        CANT_RETURN_KEY to false
    )

    private val returnSuppliesMap = mapOf(
        GENERIC_INS_NAME_KEY to true,
        GENERIC_MED_NAME_KEY to false,
        CANT_EXISTS_KEY to true,
        CANT_TRANSFER_KEY to false,
        VEHICLE_CODE_KEY to false,
        VEHICLE_TYPE_KEY to false,
        CANT_RETURN_KEY to true
    )

    private val returnMedMap = mapOf(
        GENERIC_INS_NAME_KEY to false,
        GENERIC_MED_NAME_KEY to true,
        CANT_EXISTS_KEY to true,
        CANT_TRANSFER_KEY to false,
        VEHICLE_CODE_KEY to false,
        VEHICLE_TYPE_KEY to false,
        CANT_RETURN_KEY to true
    )

    fun initScreen(inventoryName: String) {
        val inventoryType = InventoryType.from(inventoryName)

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

    @Suppress("ComplexMethod")
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

        if (chipSelectionAction.identifier == MOV_TYPE_KEY) {
            chipSelectionValues.remove(ELEMENT_TYPE_KEY)
            updateBodyModel(
                uiModels = updatedBody,
                identifier = ELEMENT_TYPE_KEY,
                updater = { model ->
                    if (model is ChipSelectionUiModel) {
                        model.copy(selected = null)
                    } else {
                        model
                    }
                }
            ).also { body -> updatedBody = body }
        }

        val movType = chipSelectionValues[MOV_TYPE_KEY]
        val chipId = chipSelectionAction.chipSelectionItemUiModel.id

        val viewVisibility = when {
            movType == null && chipId == SUPPLIES -> suppliesMap
            movType == null && chipId == MEDICATION -> medMap
            movType?.id == TRANSFER && chipId == TRANSFER -> transferMap
            movType?.id == TRANSFER && chipId == SUPPLIES -> transferSuppliesMap
            movType?.id == TRANSFER && chipId == MEDICATION -> transferMedMap
            movType?.id == RETURN && chipId == RETURN -> returnMap
            movType?.id == RETURN && chipId == SUPPLIES -> returnSuppliesMap
            movType?.id == RETURN && chipId == MEDICATION -> returnMedMap
            else -> emptyMap()
        }

        viewVisibility.forEach { viewsVisibility ->
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
                dropDownValue[viewsVisibility.key] = DropDownInputUiModel(viewsVisibility.key)
            } else {
                dropDownValue.remove(viewsVisibility.key)
            }
            model.copy(
                selected = "",
                required = viewsVisibility.value,
                visibility = viewsVisibility.value
            )
        }

        is LabelUiModel -> model.copy(text = "0")

        is RichLabelUiModel -> model.copy(
            text = "",
            visibility = viewsVisibility.value
        )

        is TextFieldUiModel -> {
            if (viewsVisibility.value) {
                fieldsValues[viewsVisibility.key] = InputUiModel(
                    identifier = viewsVisibility.key,
                    updatedValue = ""
                )
            } else {
                fieldsValues.remove(viewsVisibility.key)
            }
            model.copy(
                text = "",
                visibility = viewsVisibility.value
            )
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
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
                    }.onFailure {
                        UnauthorizedEventHandler.publishUnauthorizedEvent(it.toString())
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
