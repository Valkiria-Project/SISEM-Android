package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SendMedicalHistory
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.model.MedicineModel
import com.skgtecnologia.sisem.ui.navigation.model.MedicalHistoryNavigationModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

private const val DATE_FORMAT = "HH:mm"
private const val SAVE_COLOR = "#3cf2dd"

@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val getMedicalHistoryScreen: GetMedicalHistoryScreen,
    private val sendMedicalHistory: SendMedicalHistory,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryUiState())
        private set

    private val allowInfoCardIdentifiers = listOf(
        "INITIAL_VITAL_SIGNS",
        "DURING_VITAL_SIGNS",
        "END_VITAL_SIGNS"
    )
    private var temporalInfoCard by mutableStateOf("")
    private var temporalMedsSelector by mutableStateOf("")
    private var vitalSignsValues = mutableStateMapOf<String, List<String>>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getMedicalHistoryScreen.invoke(
                serial = androidIdProvider.getAndroidId(),
                incidentCode = "101",
                patientId = "13"
            ).onSuccess {
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
                        errorModel = throwable.mapToUi()
                    )
                }
            }
        }
    }

    fun showVitalSignsForm(identifier: String) {
        if (allowInfoCardIdentifiers.contains(identifier)) {
            temporalInfoCard = identifier
            uiState = uiState.copy(
                navigationModel = MedicalHistoryNavigationModel(
                    isInfoCardEvent = true
                )
            )
        }
    }

    fun updateVitalSignsInfoCard(values: List<String>) {
        vitalSignsValues[temporalInfoCard] = values

        val updateBody = uiState.screenModel?.body?.map { bodyRowModel ->
            if (bodyRowModel is InfoCardUiModel && bodyRowModel.identifier == temporalInfoCard) {
                bodyRowModel.copy(
                    pill = PillUiModel(
                        title = TextUiModel(
                            text = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern(DATE_FORMAT)
                            ),
                            textStyle = TextStyle.BUTTON_1
                        ),
                        color = SAVE_COLOR
                    ),
                    chipSection = bodyRowModel.chipSection?.listText?.copy(texts = values)?.let {
                        bodyRowModel.chipSection?.copy(
                            listText = it
                        )
                    }
                )
            } else {
                bodyRowModel
            }
        }.orEmpty()

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updateBody
            )
        )
    }

    fun showMedicineForm(identifier: String) {
        temporalMedsSelector = identifier
        uiState = uiState.copy(
            navigationModel = MedicalHistoryNavigationModel(
                isMedsSelectorEvent = true
            )
        )
    }

    fun updateMedicineInfoCard(medicine: MedicineModel) {
        val updateBody = uiState.screenModel?.body?.map { bodyRowModel ->
            if (bodyRowModel is MedsSelectorUiModel
                && bodyRowModel.identifier == temporalMedsSelector
            ) {
                val medicines = buildList {
                    addAll(bodyRowModel.medicines)
                    add(buildMedicine(medicine))
                }
                bodyRowModel.copy(
                    medicines = medicines
                )
            } else {
                bodyRowModel
            }
        }.orEmpty()

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updateBody
            )
        )
    }

    private fun buildMedicine(medicine: MedicineModel): InfoCardUiModel = InfoCardUiModel(
        identifier = UUID.randomUUID().toString(),
        icon = "ic_pill",
        title = TextUiModel(
            text = medicine.title,
            textStyle = TextStyle.HEADLINE_4
        ),
        pill = PillUiModel(
            title = TextUiModel(
                text = medicine.date,
                textStyle = TextStyle.BUTTON_1
            ),
            color = "#3E4146"
        ),
        date = null,
        chipSection = ChipSectionUiModel(
            title = TextUiModel(
                text = "Especificaciones",
                textStyle = TextStyle.HEADLINE_7
            ),
            listText = ListTextUiModel(
                texts = medicine.specifications,
                textStyle = TextStyle.BUTTON_1
            )
        ),
        reportsDetail = null,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 8.dp)
    )

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }
}
