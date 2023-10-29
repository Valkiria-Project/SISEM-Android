package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLICATION_TIME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLIED_DOSES
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLIED_DOSE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.CODE
import com.skgtecnologia.sisem.domain.medicalhistory.model.CODE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DATE_MEDICINE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DOSE_UNIT_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DURING_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.END_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.FC_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GENERIC_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRM_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRO_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRV_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_RTS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_TOTAL_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.INITIAL_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_WEEKS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.TAS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SendMedicalHistory
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.button.ImageButtonSectionUiModel
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.signature.SignatureUiModel
import com.valkiria.uicomponents.components.slider.SliderUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.utlis.HOURS_MINUTES_24_HOURS_PATTERN
import com.valkiria.uicomponents.utlis.WEEK_DAYS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

private const val SAVED_VITAL_SIGNS_COLOR = "#3cf2dd"

// FIXME: Split into use cases
@Suppress("TooManyFunctions")
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
        INITIAL_VITAL_SIGNS,
        DURING_VITAL_SIGNS,
        END_VITAL_SIGNS
    )

    val glasgowIdentifier = listOf(
        GLASGOW_MRO_KEY,
        GLASGOW_MRV_KEY,
        GLASGOW_MRM_KEY
    )

    var temporalInfoCard by mutableStateOf("")
    var temporalMedsSelector by mutableStateOf("")
    var temporalSignature by mutableStateOf("")

    private var chipOptionValues = mutableStateMapOf<String, MutableList<String>>()
    private var chipSelectionValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    private var dropDownValues = mutableStateMapOf<String, DropDownInputUiModel>()
    private var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    private var humanBodyValues = mutableStateListOf<HumanBodyUi>()
    private var imageButtonSectionValues = mutableStateMapOf<String, String>()
    private var medicineValues = mutableListOf<Map<String, String>>()
    private var segmentedValues = mutableStateMapOf<String, Boolean>()
    private var signatureValues = mutableStateMapOf<String, String>()
    private var sliderValues = mutableStateMapOf<String, String>()
    var vitalSignsValues = mutableStateMapOf<String, Map<String, String>>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getMedicalHistoryScreen.invoke(
                serial = androidIdProvider.getAndroidId(),
                incidentCode = "101",
                patientId = "14"
            ).onSuccess { medicalHistoryScreenModel ->
                medicalHistoryScreenModel.getFormInitialValues()

                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        screenModel = medicalHistoryScreenModel
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

    @Suppress("ComplexMethod")
    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is ChipOptionsUiModel -> {
                    val selectedOptions = bodyRowModel.items
                        .filter { it.selected }
                        .map { it.id }

                    if (selectedOptions.isEmpty().not()) {
                        chipOptionValues[bodyRowModel.identifier] = selectedOptions.toMutableList()
                    }
                }

                is ChipSelectionUiModel -> bodyRowModel.selected?.let {
                    bodyRowModel.items.find {
                        it.id == bodyRowModel.selected
                    }?.also {
                        chipSelectionValues[bodyRowModel.identifier] = it
                    }
                }

                is DropDownUiModel -> bodyRowModel.items.find {
                    it.id == bodyRowModel.selected
                }?.also {
                    dropDownValues[bodyRowModel.identifier] = DropDownInputUiModel(
                        bodyRowModel.identifier,
                        it.id,
                        it.name
                    )
                }

                is ImageButtonSectionUiModel -> {
                    Timber.d("No selected property for this one") // FIXME: Backend
                }

                is SegmentedSwitchUiModel ->
                    segmentedValues[bodyRowModel.identifier] = bodyRowModel.selected

                is SliderUiModel ->
                    sliderValues[bodyRowModel.identifier] = bodyRowModel.selected.toString()

                is TextFieldUiModel ->
                    fieldsValues[bodyRowModel.identifier] = InputUiModel(bodyRowModel.identifier)

                else -> Timber.d("no-op")
            }
        }
    }

    fun handleChipOptionAction(chipOptionAction: GenericUiAction.ChipOptionAction) {
        var chipOption = chipOptionValues[chipOptionAction.identifier]

        when {
            chipOption != null && chipOption.contains(chipOptionAction.chipOptionUiModel.id) ->
                chipOption.remove(chipOptionAction.chipOptionUiModel.id)

            chipOption != null &&
                chipOption.contains(chipOptionAction.chipOptionUiModel.id).not() -> {
                chipOption.add(chipOptionAction.chipOptionUiModel.id)
            }

            else -> {
                chipOption = mutableListOf(chipOptionAction.chipOptionUiModel.id)
            }
        }

        chipOptionValues[chipOptionAction.identifier] = chipOption.toMutableList()

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = chipOptionAction.identifier,
            updater = { model ->
                if (model is ChipOptionsUiModel) {
                    model.copy(
                        items = model.items.map {
                            if (it.id == chipOptionAction.chipOptionUiModel.id &&
                                chipOption.contains(chipOptionAction.chipOptionUiModel.id)
                            ) {
                                it.copy(selected = chipOptionAction.chipOptionUiModel.selected)
                            } else {
                                it
                            }
                        }
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

    fun handleChipSelectionAction(chipSelectionAction: GenericUiAction.ChipSelectionAction) {
        chipSelectionValues[chipSelectionAction.identifier] =
            chipSelectionAction.chipSelectionItemUiModel

        if (glasgowIdentifier.contains(chipSelectionAction.identifier)) {
            updateGlasgow()
        }

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

    @Suppress("MagicNumber", "ComplexMethod")
    private fun updateGlasgow() {
        val mro = chipSelectionValues[GLASGOW_MRO_KEY]?.name?.substring(0, 1)?.toIntOrNull() ?: 0
        val mrv = chipSelectionValues[GLASGOW_MRV_KEY]?.name?.substring(0, 1)?.toIntOrNull() ?: 0
        val mrm = chipSelectionValues[GLASGOW_MRM_KEY]?.name?.substring(0, 1)?.toIntOrNull() ?: 0
        val ecg = mro + mrv + mrm

        val tas = vitalSignsValues[INITIAL_VITAL_SIGNS]?.get(TAS_KEY)?.toIntOrNull() ?: 0
        val fc = vitalSignsValues[INITIAL_VITAL_SIGNS]?.get(FC_KEY)?.toIntOrNull() ?: 0

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

    fun handleDropDownAction(dropDownAction: GenericUiAction.DropDownAction) {
        dropDownValues[dropDownAction.identifier] = DropDownInputUiModel(
            dropDownAction.identifier,
            dropDownAction.id,
            dropDownAction.name,
            dropDownAction.fieldValidated
        )

        val updatedBody = updateBodyModel(
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

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun handleHumanBodyAction(humanBodyAction: GenericUiAction.HumanBodyAction) {
        val humanBody = humanBodyValues.find { it.area == humanBodyAction.values.area }

        if (humanBody != null) {
            humanBodyValues.remove(humanBody)
        } else {
            humanBodyValues.add(humanBodyAction.values)
        }
    }

    fun handleImageButtonAction(imageButtonAction: GenericUiAction.ImageButtonAction) {
        imageButtonSectionValues[imageButtonAction.identifier] = imageButtonAction.itemIdentifier

        // FIXME: Not working
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = imageButtonAction.identifier,
            updater = { model ->
                if (model is ImageButtonSectionUiModel) {
                    model.copy(
                        options = model.options.map {
                            it.copy(
                                options = it.options.map { imageButtonUiModel ->
                                    imageButtonUiModel.copy(
                                        selected = imageButtonUiModel.identifier ==
                                            imageButtonAction.itemIdentifier
                                    )
                                }
                            )
                        }
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

    fun handleInputAction(inputAction: GenericUiAction.InputAction) {
        fieldsValues[inputAction.identifier] = InputUiModel(
            inputAction.identifier,
            inputAction.updatedValue,
            inputAction.fieldValidated
        )

        if (inputAction.identifier == FUR_KEY) {
            updateFurAndGestationWeeks()
        }

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = inputAction.identifier,
            updater = { model ->
                if (model is TextFieldUiModel) {
                    model.copy(value = inputAction.updatedValue)
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

    private fun updateFurAndGestationWeeks() {
        val updatedBody = uiState.screenModel?.body?.map {
            when {
                (it is LabelUiModel && it.identifier == PREGNANT_FUR_KEY) -> {
                    val temporalFurModel = it.copy(
                        text = fieldsValues[FUR_KEY]?.updatedValue.orEmpty()
                    )

                    temporalFurModel
                }

                (it is LabelUiModel && it.identifier == PREGNANT_WEEKS_KEY) -> {
                    val gestationWeeks = calculateGestationWeeks()

                    val temporalWeeksModel = it.copy(
                        text = gestationWeeks
                    )

                    temporalWeeksModel
                }

                else -> it
            }
        }.orEmpty()

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun handleSegmentedSwitchAction(segmentedSwitchAction: GenericUiAction.SegmentedSwitchAction) {
        segmentedValues[segmentedSwitchAction.identifier] = segmentedSwitchAction.status

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = segmentedSwitchAction.identifier,
            updater = { model ->
                if (model is SegmentedSwitchUiModel) {
                    model.copy(selected = segmentedSwitchAction.status)
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

    fun handleSliderAction(sliderAction: GenericUiAction.SliderAction) {
        sliderValues[sliderAction.identifier] = sliderAction.value.toString()

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = sliderAction.identifier,
            updater = { model ->
                if (model is SliderUiModel) {
                    model.copy(selected = sliderAction.value)
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

    fun updateVitalSignsInfoCard(values: Map<String, String>) {
        vitalSignsValues[temporalInfoCard] = values

        if (temporalInfoCard == INITIAL_VITAL_SIGNS) {
            updateGlasgow()
        }

        val list = values.map { "${it.key} ${it.value}" }

        val updatedBody = uiState.screenModel?.body?.map { bodyRowModel ->
            if (bodyRowModel is InfoCardUiModel && bodyRowModel.identifier == temporalInfoCard) {
                bodyRowModel.copy(
                    pill = PillUiModel(
                        title = TextUiModel(
                            text = LocalDateTime.now().format(
                                DateTimeFormatter.ofPattern(HOURS_MINUTES_24_HOURS_PATTERN)
                            ),
                            textStyle = TextStyle.BUTTON_1
                        ),
                        color = SAVED_VITAL_SIGNS_COLOR
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
                body = updatedBody
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

    fun updateMedicineInfoCard(medicine: Map<String, String>) {
        medicineValues.add(medicine)
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is MedsSelectorUiModel && it.identifier == temporalMedsSelector) {
                val medicines = buildList {
                    addAll(it.medicines)
                    add(buildMedicine(medicine))
                }
                it.copy(
                    medicines = medicines
                )
            } else {
                it
            }
        }.orEmpty()

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    private fun buildMedicine(medicine: Map<String, String>): InfoCardUiModel = InfoCardUiModel(
        identifier = UUID.randomUUID().toString(),
        icon = "ic_pill",
        title = TextUiModel(
            text = medicine[CODE_KEY]?.split(" - ")?.firstOrNull().orEmpty(),
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
        val presentation = medicine[CODE_KEY]?.split(" - ")?.lastOrNull()
        return listOf(
            "$APPLIED_DOSES ${medicine[APPLIED_DOSE_KEY]} ${medicine[DOSE_UNIT_KEY]}",
            "$CODE ${medicine[GENERIC_NAME_KEY]}",
            "$QUANTITY_USED ${medicine[QUANTITY_USED_KEY]} $presentation",
            "$ADMINISTRATION_ROUTE ${medicine[ADMINISTRATION_ROUTE_KEY]}"
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

    fun updateSignature(signature: String) {
        signatureValues[temporalSignature] = signature
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is SignatureUiModel && it.identifier == temporalSignature) {
                val temporalSignatureModel = it.copy(
                    signature = signature
                )

                temporalSignatureModel
            } else {
                it
            }
        }.orEmpty()

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    private fun calculateGestationWeeks(): String {
        val fur = LocalDate.parse(fieldsValues[FUR_KEY]?.updatedValue.orEmpty())
        val now = LocalDate.now()
        return ((now.toEpochDay() - fur.toEpochDay()) / WEEK_DAYS).toString()
    }

    fun sendMedicalHistory() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            sendMedicalHistory.invoke(
                humanBodyValues = humanBodyValues,
                segmentedValues = segmentedValues,
                signatureValues = signatureValues,
                fieldsValue = fieldsValues.mapValues { it.value.updatedValue },
                sliderValues = sliderValues,
                dropDownValues = dropDownValues.mapValues { it.value.id },
                chipSelectionValues = chipSelectionValues.mapValues { it.value.id },
                chipOptionsValues = chipOptionValues,
                imageButtonSectionValues = imageButtonSectionValues,
                vitalSigns = vitalSignsValues,
                infoCardButtonValues = medicineValues
            ).onSuccess {
                Timber.d("This is a success")
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    infoEvent = throwable.mapToUi()
                )
            }
        }
    }

    fun handleShownError() {
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
}
