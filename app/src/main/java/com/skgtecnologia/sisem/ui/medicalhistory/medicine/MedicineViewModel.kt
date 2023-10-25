package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicineScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val GENERIC_NAME_KEY = "KEY_GENERIC_NAME"
const val DATE_MEDICINE_KEY = "KEY_APPLICATION_DATE"
const val APPLICATION_TIME_KEY = "KEY_APPLICATION_TIME"
const val DOSE_UNIT_KEY = "KEY_DOSE_UNIT"
const val APPLIED_DOSE_KEY = "KEY_DOSE_APPLIED"
const val CODE_KEY = "CODE_KEY"
const val QUANTITY_USED_KEY = "KEY_CANT_APPLIED"
const val ADMINISTRATION_ROUTE_KEY = "KEY_VIA_ADMIN"

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getMedicineScreen: GetMedicineScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicineUiState())
        private set

    var chipValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    var dropDownValue = mutableStateOf(DropDownInputUiModel())
    var timePickerValue = mutableStateOf("")

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getMedicineScreen.invoke()
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            screenModel = it
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

        // FIXME: 2021-10-14 Validate chip values ???
        // val areValidChip = chipValues.size == 2

        if (areValidFields && isValidDropDown) {
            uiState = uiState.copy(
                navigationModel = MedicineNavigationModel(
                    values = buildMedicineInformation()
                )
            )
        }
    }

    private fun buildMedicineInformation(): Map<String, String> = mapOf(
        GENERIC_NAME_KEY to dropDownValue.value.id,
        CODE_KEY to dropDownValue.value.name,
        DATE_MEDICINE_KEY to fieldsValues[DATE_MEDICINE_KEY]?.updatedValue.orEmpty(),
        APPLICATION_TIME_KEY to timePickerValue.value,
        DOSE_UNIT_KEY to chipValues[DOSE_UNIT_KEY]?.id.orEmpty(),
        APPLIED_DOSE_KEY to fieldsValues[APPLIED_DOSE_KEY]?.updatedValue.orEmpty(),
        QUANTITY_USED_KEY to fieldsValues[QUANTITY_USED_KEY]?.updatedValue.orEmpty(),
        ADMINISTRATION_ROUTE_KEY to chipValues[ADMINISTRATION_ROUTE_KEY]?.id.orEmpty()
    )

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
