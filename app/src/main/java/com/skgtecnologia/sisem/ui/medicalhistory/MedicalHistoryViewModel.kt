package com.skgtecnologia.sisem.ui.medicalhistory

import android.net.Uri
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
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.medicalhistory.model.ACCEPT_TRANSFER_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.ALIVE_KEY
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
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLUCOMETRY_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.INITIAL_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_WEEKS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.TAS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.TEMPERATURE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SendMedicalHistory
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.medicalHistorySuccess
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.button.ImageButtonSectionUiModel
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.signature.SignatureUiModel
import com.valkiria.uicomponents.components.slider.SliderUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.utlis.HOURS_MINUTES_24_HOURS_PATTERN
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDate
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
private const val TEMPERATURE_SYMBOL = "°C"
private const val GLUCOMETRY_SYMBOL = "mg/dL"

// FIXME: Split into use cases
@Suppress("LargeClass", "TooManyFunctions", "LongMethod", "ComplexMethod")
@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    private val getMedicalHistoryScreen: GetMedicalHistoryScreen,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val sendMedicalHistory: SendMedicalHistory,
    androidIdProvider: AndroidIdProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryUiState())
        private set

    private var initialVitalSignsTas: Int = 0
    private var initialVitalSignsFc: Int = 0
    private var vitalSignsChipSection: ChipSectionUiModel? = null

    private val allowInfoCardIdentifiers = listOf(
        INITIAL_VITAL_SIGNS,
        DURING_VITAL_SIGNS,
        END_VITAL_SIGNS
    )

    private val glasgowIdentifier = listOf(
        GLASGOW_MRO_KEY,
        GLASGOW_MRV_KEY,
        GLASGOW_MRM_KEY
    )

    private var temporalInfoCard by mutableStateOf("")
    private var temporalMedsSelector by mutableStateOf("")
    private var temporalSignature by mutableStateOf("")

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
    private var vitalSignsValues = mutableStateMapOf<String, Map<String, String>>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
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
                        errorEvent = throwable.mapToUi()
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

                is SegmentedSwitchUiModel ->
                    segmentedValues[bodyRowModel.identifier] = bodyRowModel.selected

                is SliderUiModel ->
                    sliderValues[bodyRowModel.identifier] = bodyRowModel.selected.toString()

                is TextFieldUiModel -> fieldsValues[bodyRowModel.identifier] = InputUiModel(
                    identifier = bodyRowModel.identifier,
                    updatedValue = bodyRowModel.text,
                    fieldValidated = bodyRowModel.text.isNotEmpty(),
                    required = bodyRowModel.required
                )

                is InfoCardUiModel -> if (bodyRowModel.identifier == INITIAL_VITAL_SIGNS) {
                    initialVitalSignsTas = bodyRowModel.chipSection?.listText?.texts?.find {
                        it.startsWith(TAS_KEY)
                    }?.substringAfter(TAS_KEY)?.trim()?.toInt() ?: 0

                    initialVitalSignsFc = bodyRowModel.chipSection?.listText?.texts?.find {
                        it.startsWith(FC_KEY)
                    }?.substringAfter(FC_KEY)?.trim()?.toInt() ?: 0
                } else {
                    vitalSignsChipSection = bodyRowModel.chipSection
                }
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

    @Suppress("MagicNumber", "ComplexMethod")
    private fun updateGlasgow() {
        val mro = chipSelectionValues[GLASGOW_MRO_KEY]?.name?.substring(0, 1)?.toIntOrNull() ?: 0
        val mrv = chipSelectionValues[GLASGOW_MRV_KEY]?.name?.substring(0, 1)?.toIntOrNull() ?: 0
        val mrm = chipSelectionValues[GLASGOW_MRM_KEY]?.name?.substring(0, 1)?.toIntOrNull() ?: 0
        val ecg = mro + mrv + mrm

        initialVitalSignsTas =
            if (vitalSignsValues[INITIAL_VITAL_SIGNS]?.get(TAS_KEY)?.isNotEmpty() == true) {
                vitalSignsValues[INITIAL_VITAL_SIGNS]?.get(TAS_KEY)?.toIntOrNull() ?: 0
            } else {
                initialVitalSignsTas
            }

        initialVitalSignsFc =
            if (vitalSignsValues[INITIAL_VITAL_SIGNS]?.get(FC_KEY)?.isNotEmpty() == true) {
                vitalSignsValues[INITIAL_VITAL_SIGNS]?.get(FC_KEY)?.toIntOrNull() ?: 0
            } else {
                initialVitalSignsFc
            }

        val ecgScore = when (ecg) {
            in 13..15 -> 4
            in 9..12 -> 3
            in 6..8 -> 2
            in 4..5 -> 1
            else -> 0
        }

        val tasScore = when {
            initialVitalSignsTas > 89 -> 4
            initialVitalSignsTas in 76..89 -> 3
            initialVitalSignsTas in 50..75 -> 2
            initialVitalSignsTas in 1..49 -> 1
            else -> 0
        }

        val fcScore = when {
            initialVitalSignsFc > 29 -> 4
            initialVitalSignsFc in 10..29 -> 3
            initialVitalSignsFc in 6..9 -> 2
            initialVitalSignsFc in 1..5 -> 1
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
        if (allowInfoCardIdentifiers.contains(identifier) && segmentedValues[ALIVE_KEY] == true) {
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
            inputAction.fieldValidated,
            inputAction.required
        )

        if (inputAction.identifier == FUR_KEY) {
            updateFurAndGestationWeeks()
        }

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

        var updatedBody = updateBodyModel(
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

        val viewsVisibility =
            if (
                segmentedSwitchAction.identifier == ACCEPT_TRANSFER_KEY &&
                !segmentedSwitchAction.status
            ) {
                segmentedSwitchAction.viewsVisibility.map { entry ->
                    entry.key to true
                }.toMap()
            } else {
                segmentedSwitchAction.viewsVisibility
            }

        viewsVisibility.forEach { viewVisibility ->
            updateBodyModel(
                uiModels = updatedBody,
                identifier = viewVisibility.key
            ) { model ->
                updateComponentVisibility(model, viewVisibility)
            }.also { body -> updatedBody = body }
        }

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    private fun updateComponentVisibility(
        model: BodyRowModel,
        viewsVisibility: Map.Entry<String, Boolean>
    ) = when (model) {
        is ChipOptionsUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                chipOptionValues.remove(viewsVisibility.key)
                model.copy(
                    items = model.items.map { item -> item.copy(selected = false) },
                    visibility = viewsVisibility.value
                )
            }
        }

        is ChipSelectionUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                chipSelectionValues.remove(viewsVisibility.key)
                model.copy(
                    selected = null,
                    visibility = viewsVisibility.value
                )
            }
        }

        is DetailedInfoListUiModel -> model.copy(visibility = viewsVisibility.value)

        is DropDownUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                dropDownValues.remove(viewsVisibility.key)
                model.copy(
                    selected = "",
                    visibility = viewsVisibility.value
                )
            }
        }

        is HeaderUiModel -> model.copy(visibility = viewsVisibility.value)

        is ImageButtonSectionUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                imageButtonSectionValues.remove(viewsVisibility.key)
                model.copy(
                    options = model.options.map { option ->
                        option.copy(
                            options = option.options.map { imageButtonUiModel ->
                                imageButtonUiModel.copy(
                                    selected = false
                                )
                            }
                        )
                    },
                    visibility = viewsVisibility.value
                )
            }
        }

        is InfoCardUiModel -> {
            when {
                viewsVisibility.key == INITIAL_VITAL_SIGNS || viewsVisibility.value ->
                    model.copy(visibility = viewsVisibility.value)

                else -> {
                    vitalSignsValues.remove(viewsVisibility.key)
                    model.copy(
                        chipSection = vitalSignsChipSection,
                        visibility = viewsVisibility.value
                    )
                }
            }
        }

        is LabelUiModel -> model.copy(visibility = viewsVisibility.value)

        is RichLabelUiModel -> model.copy(visibility = viewsVisibility.value)

        is SignatureUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                signatureValues.remove(viewsVisibility.key)
                model.copy(
                    signature = null,
                    visibility = viewsVisibility.value
                )
            }
        }

        is SliderUiModel -> {
            if (viewsVisibility.value) {
                model.copy(visibility = viewsVisibility.value)
            } else {
                sliderValues.remove(viewsVisibility.key)
                model.copy(
                    selected = 0,
                    visibility = viewsVisibility.value
                )
            }
        }

        is TextFieldUiModel -> {
            if (viewsVisibility.value) {
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

        val list = values.map {
            when (it.key) {
                TEMPERATURE_KEY -> "${it.key} ${it.value} $TEMPERATURE_SYMBOL"
                GLUCOMETRY_KEY -> "${it.key} ${it.value} $GLUCOMETRY_SYMBOL"
                else -> "${it.key} ${it.value}"
            }
        }

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
        val updatedBody = uiState.screenModel?.body?.map { bodyRow ->
            if (bodyRow is MedsSelectorUiModel && bodyRow.identifier == temporalMedsSelector) {
                val medicines = buildList {
                    addAll(bodyRow.medicines)
                    add(buildMedicine(medicine))
                }.sortedWith(
                    compareByDescending<InfoCardUiModel> { it.date?.text }
                        .thenByDescending { it.title.text }
                )
                bodyRow.copy(
                    medicines = medicines
                )
            } else {
                bodyRow
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
        val fur = getLocalDate(fieldsValues[FUR_KEY]?.updatedValue.orEmpty())
        val now = LocalDate.now()
        return ((now.toEpochDay() - fur.toEpochDay()) / WEEK_DAYS).toString()
    }

    fun sendMedicalHistory() {
        uiState = uiState.copy(
            validateFields = true
        )

        val invalidRequiredFields = fieldsValues.values
            .filter { it.required && !it.fieldValidated }

        val invalidOptionalFields = fieldsValues.values
            .filter { it.required.not() && it.updatedValue.isNotEmpty() && !it.fieldValidated }

        if (invalidRequiredFields.isEmpty() && invalidOptionalFields.isEmpty()) {
            send()
        }
    }

    private fun send() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch {
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
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        infoEvent = medicalHistorySuccess().mapToUi()
                    )
                }
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorEvent = throwable.mapToUi()
                    )
                }
            }
        }
    }

    fun showCamera() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryNavigationModel(
                showCamera = true
            )
        )
    }

    fun onPhotoTaken(savedUri: Uri) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)
            add(savedUri)
        }

        uiState = uiState.copy(
            selectedMediaUris = updatedSelectedMedia,
            navigationModel = MedicalHistoryNavigationModel(
                photoTaken = true
            )
        )
    }

    fun updateMediaActions(selectedMedia: List<Uri>? = null) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)

            if (selectedMedia?.isNotEmpty() == true) addAll(selectedMedia)
        }

        val updatedBody = uiState.screenModel?.body?.map { model ->
            if (model is MediaActionsUiModel) {
                model.copy(selectedMediaUris = updatedSelectedMedia)
            } else {
                model
            }
        }.orEmpty()

        uiState = uiState.copy(
            selectedMediaUris = if (selectedMedia == null) {
                uiState.selectedMediaUris
            } else {
                updatedSelectedMedia
            },
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    fun removeMediaActionsImage(selectedMedia: Uri) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)

            removeIf { uri ->
                selectedMedia.toString() == uri.toString()
            }
        }

        val updatedBody = uiState.screenModel?.body?.map { model ->
            if (model is MediaActionsUiModel) {
                model.copy(selectedMediaUris = updatedSelectedMedia)
            } else {
                model
            }
        }.orEmpty()

        uiState = uiState.copy(
            selectedMediaUris = updatedSelectedMedia,
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
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
            errorEvent = null
        )
    }

    fun consumeShownInfoEvent() {
        uiState = uiState.copy(
            infoEvent = null,
            navigationModel = MedicalHistoryNavigationModel(back = true)
        )
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = MedicalHistoryNavigationModel(back = true)
        )
    }
}
