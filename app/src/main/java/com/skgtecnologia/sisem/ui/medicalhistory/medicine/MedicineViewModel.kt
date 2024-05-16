package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.BuildMedicineInformation
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicineScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.timepicker.TimePickerUiModel
import com.valkiria.uicomponents.utlis.TimeUtils.isSameDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val APPLICATION_DATE_KEY = "KEY_APPLICATION_DATE"
private const val APPLICATION_TIME_KEY = "KEY_APPLICATION_TIME"

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val buildMedicineInformation: BuildMedicineInformation,
    private val getMedicineScreen: GetMedicineScreen,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicineUiState())
        private set

    private var chipValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    private var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    private var dropDownValue = mutableStateOf(DropDownInputUiModel())
    private var timePickerValue = mutableStateOf("")

    private var initialTimeValue = GenericUiAction.TimePickerAction(
        hour = "00",
        minute = "00",
        identifier = APPLICATION_TIME_KEY
    )

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getMedicineScreen.invoke(serial = androidIdProvider.getAndroidId())
                .onSuccess { medicinesScreenModel ->
                    medicinesScreenModel.getFormInitialValues()
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            screenModel = medicinesScreenModel
                        )
                    }
                }.onFailure { throwable ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            infoEvent = throwable.mapToUi()
                        )
                    }
                }
        }
    }

    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is TextFieldUiModel -> fieldsValues[bodyRowModel.identifier] = InputUiModel(
                    identifier = bodyRowModel.identifier,
                    updatedValue = bodyRowModel.text,
                    fieldValidated = bodyRowModel.text.isNotEmpty(),
                    required = bodyRowModel.required
                )

                is TimePickerUiModel -> {
                    initialTimeValue = GenericUiAction.TimePickerAction(
                        hour = bodyRowModel.hour.text,
                        minute = bodyRowModel.minute.text,
                        identifier = bodyRowModel.identifier
                    )
                    timePickerValue.value =
                        "${bodyRowModel.hour.text} : ${bodyRowModel.minute.text}"
                }
            }
        }
    }

    fun handleChipSelectionAction(chipSelectionAction: GenericUiAction.ChipSelectionAction) {
        chipValues[chipSelectionAction.identifier] = chipSelectionAction.chipSelectionItemUiModel

        val updatedBody = updateBodyModel(
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

        var updatedBody = updateBodyModel(
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

        if (inputAction.identifier == APPLICATION_DATE_KEY) {
            updateBodyModel(
                uiModels = updatedBody,
                identifier = APPLICATION_TIME_KEY,
                updater = { model ->
                    when {
                        model is TimePickerUiModel && isSameDay(inputAction.updatedValue) -> {
                            timePickerValue.value =
                                "${initialTimeValue.hour} : ${initialTimeValue.minute}"

                            model.copy(
                                date = inputAction.updatedValue,
                                hour = model.hour.copy(text = initialTimeValue.hour),
                                minute = model.minute.copy(text = initialTimeValue.minute)
                            )
                        }

                        model is TimePickerUiModel && !isSameDay(inputAction.updatedValue) ->
                            model.copy(date = inputAction.updatedValue)

                        else -> model
                    }
                }
            ).also { body -> updatedBody = body }
        }

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun handleDropDownAction(dropDownAction: GenericUiAction.DropDownAction) {
        dropDownValue.value = DropDownInputUiModel(
            dropDownAction.identifier,
            dropDownAction.id,
            dropDownAction.name,
            dropDownAction.quantity,
            dropDownAction.fieldValidated
        )

        var updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = dropDownAction.identifier,
            updater = { model ->
                if (model is DropDownUiModel) {
                    model.copy(selected = dropDownAction.name)
                } else {
                    model
                }
            }
        )

        updateBodyModel(
            uiModels = updatedBody,
            identifier = QUANTITY_USED_KEY
        ) { model ->
            if (model is TextFieldUiModel) {
                model.copy(quantity = dropDownAction.quantity)
            } else {
                model
            }
        }.also { body -> updatedBody = body }

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun handleTimePickerAction(timePickerAction: GenericUiAction.TimePickerAction) {
        timePickerValue.value = "${timePickerAction.hour} : ${timePickerAction.minute}"

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = timePickerAction.identifier,
            updater = { model ->
                if (model is TimePickerUiModel) {
                    model.copy(
                        hour = model.hour.copy(text = timePickerAction.hour),
                        minute = model.minute.copy(text = timePickerAction.minute)
                    )
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

    fun saveMedicine() {
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
                navigationModel = MedicineNavigationModel(
                    values = buildMedicineInformation.invoke(
                        dropDownValue,
                        fieldsValues,
                        timePickerValue,
                        chipValues
                    )
                )
            )
        }
    }

    fun consumeInfoEvent() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun navigateBack() {
        uiState = uiState.copy(
            navigationModel = MedicineNavigationModel(
                goBack = true
            )
        )
    }
}
