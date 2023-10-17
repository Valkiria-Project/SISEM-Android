package com.skgtecnologia.sisem.ui.medicalhistory.medsselector

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicineScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.model.MedicineModel
import com.skgtecnologia.sisem.ui.navigation.model.MedicineNavigationModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val DATE_MEDICINE_KEY = "KEY_APPLICATION_DATE"
private const val APPLIED_DOSE_KEY = "KEY_DOSE_APPLIED"
private const val DOSE_UNIT_KEY = "KEY_DOSE_UNIT"
private const val QUANTITY_USED_KEY = "KEY_CANT_APPLIED"
private const val ADMINISTRATION_ROUTE_KEY = "KEY_VIA_ADMIN"

private const val APPLIED_DOSES = "Dosis aplicada"
private const val CODE = "Código"
private const val QUANTITY_USED = "Cantidad utilizada"
private const val ADMINISTRATION_ROUTE = "Via de administración"

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getMedicineScreen: GetMedicineScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicineUiState())
        private set

    var chipValues = mutableStateMapOf<String, String>()
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
                    medicine = MedicineModel(
                        title = dropDownValue.value.name,
                        date = "${fieldsValues[DATE_MEDICINE_KEY]?.updatedValue} - " +
                            timePickerValue.value,
                        specifications = buildSpecifications()
                    )
                )
            )
        }
    }

    private fun buildSpecifications(): List<String> = listOf(
        "$APPLIED_DOSES ${fieldsValues[APPLIED_DOSE_KEY]?.updatedValue}" +
            "${chipValues[DOSE_UNIT_KEY]}",
        "$CODE ${dropDownValue.value.id}",
        "$QUANTITY_USED ${fieldsValues[QUANTITY_USED_KEY]?.updatedValue}",
        "$ADMINISTRATION_ROUTE ${chipValues[ADMINISTRATION_ROUTE_KEY]}"
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
