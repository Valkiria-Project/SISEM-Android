package com.skgtecnologia.sisem.ui.medicalhistory.vitalsings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetVitalSignsScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

private const val MM_HG = 3
private const val TAS_KEY = "TAS"
private const val TAD_KEY = "TAD"
private const val TAM_KEY = "TAM"

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    private val getVitalSignsScreen: GetVitalSignsScreen
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(VitalSignsUiState())
        private set

    var fieldsValues = mutableStateMapOf<String, InputUiModel>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getVitalSignsScreen.invoke()
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            screenModel = it
                        )
                        it.fieldValues()
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

    private fun ScreenModel.fieldValues() {
        this.body.map {
            if (it is TextFieldUiModel) {
                fieldsValues[it.identifier] = InputUiModel(
                    it.identifier,
                    "",
                    false
                )
            }
        }
    }

    fun saveVitalSigns() {
        uiState = uiState.copy(
            validateFields = true
        )

        val areValidFields = fieldsValues
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        if (areValidFields) {
            calculateTAM()
            uiState = uiState.copy(
                navigationModel = VitalSignsNavigationModel(
                    values = fieldsValues.mapValues { it.value.updatedValue }
                )
            )
        }
    }

    @Suppress("MagicNumber")
    private fun calculateTAM() {
        val tas = fieldsValues[TAS_KEY]?.updatedValue?.toFloatOrNull() ?: 0f
        val tad = fieldsValues[TAD_KEY]?.updatedValue?.toFloatOrNull() ?: 0f

        val tam = tas + ((tas - tad) / MM_HG)
        val roundTam = (tam * 100).roundToInt() / 10f

        fieldsValues[TAM_KEY] = InputUiModel(
            identifier = TAM_KEY,
            updatedValue = roundTam.toString(),
            fieldValidated = true
        )
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
            navigationModel = VitalSignsNavigationModel(
                goBack = true
            )
        )
    }
}
