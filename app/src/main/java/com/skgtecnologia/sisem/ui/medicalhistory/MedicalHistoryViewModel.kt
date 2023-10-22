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
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.ADMINISTRATION_ROUTE_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.APPLICATION_TIME_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.APPLIED_DOSE_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.CODE_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.DATE_MEDICINE_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.DOSE_UNIT_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.GENERIC_NAME_KEY
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.QUANTITY_USED_KEY
import com.skgtecnologia.sisem.ui.navigation.model.MedicalHistoryNavigationModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val DATE_FORMAT = "HH:mm"
private const val SAVE_COLOR = "#3cf2dd"
private const val INITIAL_VITAL_SIGNS = "INITIAL_VITAL_SIGNS"
private const val DURING_VITAL_SIGNS = "DURING_VITAL_SIGNS"
private const val END_VITAL_SIGNS = "END_VITAL_SIGNS"
private const val GLASGOW_MRO_KEY = "KEY_GLASGOW_SCALE_MRO"
private const val GLASGOW_MRV_KEY = "KEY_GLASGOW_SCALE_MRV"
private const val GLASGOW_MRM_KEY = "KEY_GLASGOW_SCALE_MRM"
private const val GLASGOW_TOTAL_KEY = "GLASGOW_TOTAL_VALUE"
private const val GLASGOW_RTS_KEY = "GLASGOW_RTS"
private const val TAS_KEY = "TAS"
private const val FC_KEY = "FC"

private const val APPLIED_DOSES = "Dosis aplicada"
private const val CODE = "Código"
private const val QUANTITY_USED = "Cantidad utilizada"
private const val ADMINISTRATION_ROUTE = "Via de administración"

@Suppress("UnusedPrivateMember")
@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val getMedicalHistoryScreen: GetMedicalHistoryScreen,
    private val sendMedicalHistory: SendMedicalHistory, // FIXME: UnusedPrivateMember
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryUiState())
        private set

    private val allowInfoCardIdentifiers = listOf(
        INITIAL_VITAL_SIGNS,
        DURING_VITAL_SIGNS,
        END_VITAL_SIGNS
    )

    var temporalInfoCard by mutableStateOf("")
    var temporalMedsSelector by mutableStateOf("")
    var temporalSignature by mutableStateOf("")
    var chipSelectionValues = mutableStateMapOf<String, String>()
    var chipOptionValues = mutableStateMapOf<String, String>()
    var medicineValues = mutableListOf<Map<String, String>>()
    var vitalSignsValues = mutableStateMapOf<String, Map<String, String>>()
    var signatureValues = mutableStateMapOf<String, String>()

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

    fun updateVitalSignsInfoCard(values: Map<String, String>) {
        vitalSignsValues[temporalInfoCard] = values
        updateGlasgow()

        val list = values.map { "${it.key} ${it.value}" }

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
                    chipSection = bodyRowModel.chipSection?.listText?.copy(texts = list)?.let {
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

    fun showSignaturePad(identifier: String) {
        temporalSignature = identifier
        uiState = uiState.copy(
            navigationModel = MedicalHistoryNavigationModel(
                isSignatureEvent = true
            )
        )
    }

    @Suppress("MagicNumber", "ComplexMethod")
    fun updateGlasgow() {
        val mro = chipSelectionValues[GLASGOW_MRO_KEY]?.substring(0, 1)?.toIntOrNull() ?: 0
        val mrv = chipSelectionValues[GLASGOW_MRV_KEY]?.substring(0, 1)?.toIntOrNull() ?: 0
        val mrm = chipSelectionValues[GLASGOW_MRM_KEY]?.substring(0, 1)?.toIntOrNull() ?: 0
        val ecg = mro + mrv + mrm

        val tas = vitalSignsValues[DURING_VITAL_SIGNS]?.get(TAS_KEY)?.toIntOrNull() ?: 0
        val fc = vitalSignsValues[DURING_VITAL_SIGNS]?.get(FC_KEY)?.toIntOrNull() ?: 0

        val ecgScore = when (ecg) {
            in 13..15 -> 4
            in 9..12 -> 3
            in 6..8 -> 2
            in 4..5 -> 1
            else -> 0
        }

        val tasScore = when {
            tas > 89 -> 4
            tas in 76..89 -> 3
            tas in 50..75 -> 2
            tas in 1..49 -> 1
            else -> 0
        }

        val fcScore = when {
            fc > 29 -> 4
            fc in 10..29 -> 3
            fc in 6..9 -> 2
            fc in 1..5 -> 1
            else -> 0
        }

        val rts = ecgScore + tasScore + fcScore

        updateGlasgowValues(ecg, rts)
    }

    private fun updateGlasgowValues(ecg: Int, rts: Int) {
        val updateBody = uiState.screenModel?.body?.map { bodyRowModel ->
            if (bodyRowModel is LabelUiModel && bodyRowModel.identifier == GLASGOW_TOTAL_KEY) {
                bodyRowModel.copy(
                    text = ecg.toString()
                )
            } else if (bodyRowModel is LabelUiModel && bodyRowModel.identifier == GLASGOW_RTS_KEY) {
                bodyRowModel.copy(
                    text = rts.toString()
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

    fun updateMedicineInfoCard(medicine: Map<String, String>) {
        medicineValues.add(medicine)
        val updateBody = uiState.screenModel?.body?.map { bodyRowModel ->
            if (bodyRowModel is MedsSelectorUiModel &&
                bodyRowModel.identifier == temporalMedsSelector
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

    private fun buildMedicine(medicine: Map<String, String>): InfoCardUiModel = InfoCardUiModel(
        identifier = UUID.randomUUID().toString(),
        icon = "ic_pill",
        title = TextUiModel(
            text = medicine[GENERIC_NAME_KEY]?.split(" - ")?.firstOrNull().orEmpty(),
            textStyle = TextStyle.HEADLINE_4
        ),
        pill = PillUiModel(
            title = TextUiModel(
                text = "${medicine[DATE_MEDICINE_KEY]} - ${medicine[APPLICATION_TIME_KEY]}",
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
                texts = buildSpecifications(medicine),
                textStyle = TextStyle.BUTTON_1
            )
        ),
        reportsDetail = null,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 8.dp)
    )

    private fun buildSpecifications(medicine: Map<String, String>): List<String> {
        val presentation = medicine[GENERIC_NAME_KEY]?.split(" - ")?.lastOrNull()
        return listOf(
            "$APPLIED_DOSES ${medicine[APPLIED_DOSE_KEY]} ${medicine[DOSE_UNIT_KEY]}",
            "$CODE ${medicine[CODE_KEY]}",
            "$QUANTITY_USED ${medicine[QUANTITY_USED_KEY]} $presentation",
            "$ADMINISTRATION_ROUTE ${medicine[ADMINISTRATION_ROUTE_KEY]}"
        )
    }

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
