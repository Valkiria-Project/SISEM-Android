package com.skgtecnologia.sisem.ui.medicalhistory

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.medicalhistory.model.ACCEPT_TRANSFER_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE
import com.skgtecnologia.sisem.domain.medicalhistory.model.ADMINISTRATION_ROUTE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.ALIVE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLICATION_TIME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLIED_DOSES
import com.skgtecnologia.sisem.domain.medicalhistory.model.APPLIED_DOSE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.AUTH_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.CODE
import com.skgtecnologia.sisem.domain.medicalhistory.model.CODE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DATE_MEDICINE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DOCUMENT_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DOSE_UNIT_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.DURING_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.END_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.FC_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.FIRST_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GENERIC_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRM_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRO_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_MRV_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_RTS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLASGOW_TOTAL_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.GLUCOMETRY_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.INITIAL_VITAL_SIGNS
import com.skgtecnologia.sisem.domain.medicalhistory.model.LASTNAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_FUR_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.PREGNANT_WEEKS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED
import com.skgtecnologia.sisem.domain.medicalhistory.model.QUANTITY_USED_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.RESPONSIBLE_DOCUMENT_NUMBER_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.RESPONSIBLE_DOCUMENT_TYPE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.RESPONSIBLE_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.SECOND_LASTNAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.SECOND_NAME_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.TAS_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.TEMPERATURE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.WITHDRAWAL_DECLARATION_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.WITHDRAWAL_RESPONSIBLE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.getWithdrawalResponsibleText
import com.skgtecnologia.sisem.domain.medicalhistory.model.getWithdrawalWitnessText
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.GetMedicalHistoryScreen
import com.skgtecnologia.sisem.domain.medicalhistory.usecases.SendMedicalHistory
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.medicalHistorySuccess
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.button.ImageButtonSectionUiModel
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.PillUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
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
import com.valkiria.uicomponents.utlis.DATE_PATTERN
import com.valkiria.uicomponents.utlis.HOURS_MINUTES_SECONDS_24_HOURS_PATTERN
import com.valkiria.uicomponents.utlis.TimeUtils.getLocalDate
import com.valkiria.uicomponents.utlis.WEEK_DAYS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

private const val SAVED_VITAL_SIGNS_COLOR = "#3cf2dd"
private const val TEMPERATURE_SYMBOL = "°C"
private const val GLUCOMETRY_SYMBOL = "mg/dL"
private const val SEVEN_DAYS = 7L
private const val NINE_MONTHS = 9L

// FIXME: Split into use cases
@Suppress("LargeClass", "TooManyFunctions", "LongMethod", "ComplexMethod")
@HiltViewModel
class MedicalHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMedicalHistoryScreen: GetMedicalHistoryScreen,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val sendMedicalHistory: SendMedicalHistory,
    private val observeOperationConfig: ObserveOperationConfig,
    private val stringProvider: StringProvider
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(MedicalHistoryUiState())
        private set

    private val idAph: Int? = savedStateHandle[NavigationArgument.ID_APH]

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

    private val patientNameIdentifiers = listOf(
        FIRST_NAME_KEY,
        SECOND_NAME_KEY,
        LASTNAME_KEY,
        SECOND_LASTNAME_KEY,
        DOCUMENT_KEY,
        RESPONSIBLE_NAME_KEY,
        RESPONSIBLE_DOCUMENT_NUMBER_KEY
    )

    private var temporalInfoCard by mutableStateOf("")
    private var temporalMedsSelector by mutableStateOf("")
    private var temporalSignature by mutableStateOf("")

    private var chipOptionValues = mutableStateMapOf<String, MutableList<ChipOptionUiModel>>()
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
            getMedicalHistoryScreen.invoke(idAph = idAph.toString())
                .onSuccess { medicalHistoryScreenModel ->
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

            observeOperationConfig.invoke()
                .onSuccess { operationConfig ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            operationConfig = operationConfig
                        )
                    }
                }.onFailure { throwable ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
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

                    if (bodyRowModel.required || selectedOptions.isEmpty().not()) {
                        bodyRowModel.items.forEach { chipOption ->
                            chipOptionValues[bodyRowModel.identifier] =
                                mutableListOf(
                                    ChipOptionUiModel(
                                        id = chipOption.id,
                                        name = chipOption.name,
                                        selected = chipOption.selected
                                    )
                                )
                        }
                    }
                }

                is ChipSelectionUiModel -> {
                    if (bodyRowModel.required || !bodyRowModel.selected.isNullOrEmpty()) {
                        bodyRowModel.items.find {
                            it.id == bodyRowModel.selected
                        }?.also {
                            chipSelectionValues[bodyRowModel.identifier] =
                                it.copy(isSelected = true)
                        } ?: run {
                            chipSelectionValues[bodyRowModel.identifier] = ChipSelectionItemUiModel(
                                id = bodyRowModel.identifier,
                                name = bodyRowModel.identifier,
                                isSelected = false
                            )
                        }
                    }
                }

                is DropDownUiModel -> {
                    if (bodyRowModel.required || bodyRowModel.selected.isNotEmpty()) {
                        bodyRowModel.items.find {
                            it.id == bodyRowModel.selected
                        }?.also {
                            dropDownValues[bodyRowModel.identifier] = DropDownInputUiModel(
                                bodyRowModel.identifier,
                                it.id,
                                it.name
                            )
                        } ?: run {
                            dropDownValues[bodyRowModel.identifier] = DropDownInputUiModel()
                        }
                    }
                }

                is SegmentedSwitchUiModel ->
                    segmentedValues[bodyRowModel.identifier] = bodyRowModel.selected

                is SliderUiModel ->
                    sliderValues[bodyRowModel.identifier] = bodyRowModel.selected.toString()

                is TextFieldUiModel -> {
                    if (bodyRowModel.required || bodyRowModel.text.isNotEmpty()) {
                        fieldsValues[bodyRowModel.identifier] = InputUiModel(
                            identifier = bodyRowModel.identifier,
                            updatedValue = bodyRowModel.text,
                            fieldValidated = bodyRowModel.text.isNotEmpty(),
                            required = bodyRowModel.required
                        )
                    }
                }

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
            chipOption != null && chipOption.contains(chipOptionAction.chipOptionUiModel) ->
                chipOption.remove(chipOptionAction.chipOptionUiModel)

            chipOption != null &&
                    chipOption.contains(chipOptionAction.chipOptionUiModel).not() -> {
                chipOption.add(chipOptionAction.chipOptionUiModel)
            }

            else -> {
                chipOption = mutableListOf(chipOptionAction.chipOptionUiModel)
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
                                chipOption.contains(chipOptionAction.chipOptionUiModel)
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
            chipSelectionAction.chipSelectionItemUiModel.copy(isSelected = true)

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
            dropDownAction.quantity,
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

        if (patientNameIdentifiers.contains(inputAction.identifier)) {
            updatePatientName()
            updateWithdrawalResponsible()
            updateWithdrawalWitness()
        }

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

    private fun updatePatientName() {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = AUTH_NAME_KEY,
            updater = { model ->
                if (model is LabelUiModel) {
                    model.copy(
                        text = stringProvider.getString(
                            R.string.medical_history_proc_auth_label,
                            fieldsValues.getPatientName()
                        )
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

    private fun updateWithdrawalResponsible() {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = WITHDRAWAL_RESPONSIBLE_KEY,
            updater = { model ->
                if (model is RichLabelUiModel) {
                    model.copy(
                        text = getWithdrawalResponsibleText(
                            responsible = fieldsValues.getResponsibleName(),
                            documentType = chipSelectionValues.getResponsibleDocType(),
                            document = fieldsValues.getResponsibleDocument()
                        )
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

    private fun updateWithdrawalWitness() {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = WITHDRAWAL_DECLARATION_KEY,
            updater = { model ->
                if (model is RichLabelUiModel) {
                    model.copy(
                        text = getWithdrawalWitnessText(
                            patient = fieldsValues.getPatientName(),
                            document = fieldsValues.getPatientDocument(),
                            code = uiState.operationConfig?.vehicleCode.orEmpty()
                        )
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

    private fun SnapshotStateMap<String, InputUiModel>.getPatientName(): String =
        this[FIRST_NAME_KEY]?.updatedValue.orEmpty() + " " +
                this[SECOND_NAME_KEY]?.updatedValue.orEmpty() + " " +
                this[LASTNAME_KEY]?.updatedValue.orEmpty() + " " +
                this[SECOND_LASTNAME_KEY]?.updatedValue.orEmpty()

    private fun SnapshotStateMap<String, InputUiModel>.getPatientDocument(): String =
        this[DOCUMENT_KEY]?.updatedValue.orEmpty()

    private fun SnapshotStateMap<String, InputUiModel>.getResponsibleName(): String =
        this[RESPONSIBLE_NAME_KEY]?.updatedValue.orEmpty()

    private fun SnapshotStateMap<String, InputUiModel>.getResponsibleDocument(): String =
        this[RESPONSIBLE_DOCUMENT_NUMBER_KEY]?.updatedValue.orEmpty()

    private fun SnapshotStateMap<String, ChipSelectionItemUiModel>.getResponsibleDocType(): String =
        this[RESPONSIBLE_DOCUMENT_TYPE_KEY]?.name.orEmpty()

    private fun updateFurAndGestationWeeks() {
        val updatedBody = uiState.screenModel?.body?.map {
            when {
                (it is LabelUiModel && it.identifier == PREGNANT_FUR_KEY) -> {
                    val estimatedDueDate = calculateEstimatedDueDate()
                    val temporalFurModel = it.copy(
                        text = estimatedDueDate
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

    private fun calculateEstimatedDueDate(): String {
        val fur = fieldsValues[FUR_KEY]?.updatedValue.orEmpty()
        val furDate = getLocalDate(fur).plusDays(SEVEN_DAYS)
        val estimatedDueDate = furDate.plusMonths(NINE_MONTHS)

        return estimatedDueDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN))
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
                                DateTimeFormatter.ofPattern(HOURS_MINUTES_SECONDS_24_HOURS_PATTERN)
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
                    compareByDescending { it.date?.text }
                ).reversed()

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

    fun sendMedicalHistory(images: List<File>) {
        uiState = uiState.copy(
            isLoading = true,
            validateFields = true
        )

        val areValidFields = fieldsValues
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        val areValidChipOptions = chipOptionValues
            .mapValues {
                it.value.map { chipOption ->
                    chipOption.selected
                }
            }.map {
                it.value.contains(true)
            }

        val areValidChipSelections = chipSelectionValues
            .mapValues {
                it.value.isSelected
            }
            .containsValue(false)
            .not()

        val areValidDropDowns = dropDownValues
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        if (
            areValidFields &&
            areValidChipOptions.isNotEmpty() &&
            areValidChipSelections &&
            areValidDropDowns
        ) {
            job?.cancel()
            job = viewModelScope.launch {
                sendMedicalHistory.invoke(
                    idAph = idAph.toString(),
                    humanBodyValues = humanBodyValues,
                    segmentedValues = segmentedValues,
                    signatureValues = signatureValues,
                    fieldsValue = fieldsValues.mapValues { it.value.updatedValue },
                    sliderValues = sliderValues,
                    dropDownValues = dropDownValues.mapValues { it.value.id },
                    chipSelectionValues = chipSelectionValues.mapValues { it.value.id },
                    chipOptionsValues = chipOptionValues.mapValues { it.value.map { chipOption -> chipOption.id } },
                    imageButtonSectionValues = imageButtonSectionValues,
                    vitalSigns = vitalSignsValues,
                    infoCardButtonValues = medicineValues,
                    images = images.mapIndexed { index, image ->
                        ImageModel(
                            fileName = "Img_$idAph" + "_$index.jpg",
                            file = image
                        )
                    }
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
        } else {
            uiState = uiState.copy(
                isLoading = false
            )
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
