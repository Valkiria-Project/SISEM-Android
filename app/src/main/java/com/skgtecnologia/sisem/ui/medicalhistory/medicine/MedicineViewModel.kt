package com.skgtecnologia.sisem.ui.medicalhistory.medicine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.BuildMedicineInformation
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

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val buildMedicineInformation: BuildMedicineInformation,
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
