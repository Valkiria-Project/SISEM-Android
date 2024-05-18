package com.skgtecnologia.sisem.ui.medicalhistory

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
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
import com.skgtecnologia.sisem.domain.medicalhistory.model.WHO_WITHDRAWAL_RESPONSIBLE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.WITHDRAWAL_DECLARATION_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.WITHDRAWAL_RESPONSIBLE_KEY
import com.skgtecnologia.sisem.domain.medicalhistory.model.WITHDRAWAL_TYPE_KEY
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
import com.valkiria.uicomponents.components.chip.FiltersUiModel
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
import com.valkiria.uicomponents.utlis.DATE_TIME_PATTERN
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
private const val PATIENT_DOCUMENT_TYPE_IDENTIFIER = "KEY_DOCUMENT_TYPE"
private const val RESPONSIBLE_DOCUMENT_TYPE_IDENTIFIER = "KEY_DOCUMENT_TYPE_RESPONSIBLE"
private const val PATIENT_DOCUMENT_IDENTIFIER = "KEY_DOCUMENT"
private const val RESPONSIBLE_DOCUMENT_IDENTIFIER = "KEY_DOCUMENT_RESPONSIBLE"
private const val SIGN_PERSON_CHARGE_W_KEY = "KEY_SIGN_PERSON_CHARGE_W"
private const val WITHDRAWAL_REPONSIBLE_REFUSE_SIGN_KEY = "KEY_WITHDRAWAL_REPONSIBLE_REFUSE_SIGN"
private const val TIME_KEY = "TIME"
private const val NO = "NO"
private const val CC_ID_TYPE = "CC"
private const val CE_ID_TYPE = "CE"
private const val CD_ID_TYPE = "CD"
private const val PA_ID_TYPE = "PA"
private const val SC_ID_TYPE = "SC"
private const val PTP_ID_TYPE = "PTP"
private const val PEP_ID_TYPE = "PEP"
private const val RC_ID_TYPE = "RC"
private const val TI_ID_TYPE = "TI"
private const val NUIP_ID_TYPE = "NUIP"
private const val ASI_ID_TYPE = "ASI"
private const val MSI_ID_TYPE = "MSI"
private const val TE_ID_TYPE = "TE"
private const val EP_ID_TYPE = "EP"

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

    private val withdrawalIdentifiers = listOf(
        WITHDRAWAL_TYPE_KEY,
        WHO_WITHDRAWAL_RESPONSIBLE_KEY
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

            getMedicalHistoryScreen.invoke(idAph = idAph.toString())
                .onSuccess { medicalHistoryScreenModel ->
                    medicalHistoryScreenModel.getFormInitialValues()

                    var updatedBody = medicalHistoryScreenModel.body
                    if (chipSelectionValues.containsKey(PATIENT_DOCUMENT_TYPE_IDENTIFIER)) {
                        updatedBody = updatePatientDocumentInputType(
                            chipSelectionValues[PATIENT_DOCUMENT_TYPE_IDENTIFIER]?.id.orEmpty(),
                            PATIENT_DOCUMENT_IDENTIFIER,
                            updatedBody
                        )
                    } else if (
                        chipSelectionValues.containsKey(RESPONSIBLE_DOCUMENT_TYPE_IDENTIFIER)
                    ) {
                        updatedBody = updatePatientDocumentInputType(
                            chipSelectionValues[RESPONSIBLE_DOCUMENT_TYPE_IDENTIFIER]?.id.orEmpty(),
                            RESPONSIBLE_DOCUMENT_IDENTIFIER,
                            updatedBody
                        )
                    }

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            screenModel = medicalHistoryScreenModel.copy(
                                body = updatedBody
                            )
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

    @Suppress("ComplexMethod", "NestedBlockDepth")
    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is ChipOptionsUiModel -> {
                    val selectedOptions = bodyRowModel.items
                        .filter { it.selected }

                    if ((bodyRowModel.required && bodyRowModel.visibility) ||
                        selectedOptions.isEmpty().not()
                    ) {
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
                    if ((bodyRowModel.required && bodyRowModel.visibility) ||
                        !bodyRowModel.selected.isNullOrEmpty()
                    ) {
                        bodyRowModel.items.find {
                            it.id == bodyRowModel.selected || it.name == bodyRowModel.selected
                        }?.also {
                            chipSelectionValues[bodyRowModel.identifier] =
                                it.copy(isSelected = true)
                        } ?: run {
                            chipSelectionValues[bodyRowModel.identifier] =
                                ChipSelectionItemUiModel()
                        }
                    }
                }

                is DropDownUiModel -> {
                    if ((bodyRowModel.required && bodyRowModel.visibility) ||
                        bodyRowModel.selected.isNotEmpty()
                    ) {
                        bodyRowModel.items.find {
                            it.name.equals(bodyRowModel.selected, true)
                        }?.also {
                            dropDownValues[bodyRowModel.identifier] = DropDownInputUiModel(
                                bodyRowModel.identifier,
                                it.id,
                                it.name,
                                fieldValidated = true
                            )
                        } ?: run {
                            dropDownValues[bodyRowModel.identifier] = DropDownInputUiModel()
                        }
                    }
                }

                is ImageButtonSectionUiModel -> {
                    if ((bodyRowModel.required && bodyRowModel.visibility) ||
                        !bodyRowModel.selected.isNullOrEmpty()
                    ) {
                        imageButtonSectionValues[bodyRowModel.identifier] =
                            bodyRowModel.selected.orEmpty()
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

                is SegmentedSwitchUiModel ->
                    segmentedValues[bodyRowModel.identifier] = bodyRowModel.selected

                is SignatureUiModel ->
                    if ((bodyRowModel.required && bodyRowModel.visibility) ||
                        !bodyRowModel.signature.isNullOrEmpty()
                    ) {
                        signatureValues[bodyRowModel.identifier] = bodyRowModel.signature.orEmpty()
                    }

                is SliderUiModel ->
                    sliderValues[bodyRowModel.identifier] = bodyRowModel.selected.toString()

                is TextFieldUiModel -> {
                    if ((bodyRowModel.required && bodyRowModel.visibility) ||
                        bodyRowModel.text.isNotEmpty()
                    ) {
                        fieldsValues[bodyRowModel.identifier] = InputUiModel(
                            identifier = bodyRowModel.identifier,
                            updatedValue = bodyRowModel.text,
                            fieldValidated = bodyRowModel.text.isNotEmpty(),
                            required = bodyRowModel.required
                        )
                    }
                }
            }
        }
    }

    fun handleChipOptionAction(chipOptionAction: GenericUiAction.ChipOptionAction) {
        var chipOption = chipOptionValues[chipOptionAction.identifier]

        when {
            chipOption != null && chipOption.find {
                it.id == chipOptionAction.chipOptionUiModel.id
            } != null ->
                chipOption.remove(chipOptionAction.chipOptionUiModel)

            chipOption != null && chipOption.find {
                it.id == chipOptionAction.chipOptionUiModel.id
            } == null ->
                chipOption.add(chipOptionAction.chipOptionUiModel)

            else -> {
                chipOption = mutableListOf(chipOptionAction.chipOptionUiModel)
            }
        }

        chipOptionValues[chipOptionAction.identifier] = chipOption.toMutableList()

        var updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = chipOptionAction.identifier,
            updater = { model ->
                if (model is ChipOptionsUiModel) {
                    model.copy(
                        items = model.items.map {
                            if (it.id == chipOptionAction.chipOptionUiModel.id) {
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

        chipOptionAction.chipOptionUiModel.viewsVisibility?.forEach { viewsVisibility ->
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

    fun handleChipSelectionAction(chipSelectionAction: GenericUiAction.ChipSelectionAction) {
        chipSelectionValues[chipSelectionAction.identifier] =
            chipSelectionAction.chipSelectionItemUiModel.copy(isSelected = true)

        if (glasgowIdentifier.contains(chipSelectionAction.identifier)) {
            updateGlasgow()
        }

        if (withdrawalIdentifiers.contains(chipSelectionAction.identifier)) {
            updatePatientName()
            updateWithdrawalWitness(chipSelectionAction.chipSelectionItemUiModel.id)
            updateWithdrawalResponsible()
        }

        if (chipSelectionAction.identifier == WITHDRAWAL_REPONSIBLE_REFUSE_SIGN_KEY) {
            updateResponsibleSignature(chipSelectionAction.chipSelectionItemUiModel)
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

        chipSelectionAction.viewsInvisibility.forEach { viewsInvisibility ->
            updateBodyModel(
                uiModels = updatedBody,
                identifier = viewsInvisibility.key
            ) { model ->
                updateComponentVisibility(model, viewsInvisibility)
            }.also { body -> updatedBody = body }
        }

        if (chipSelectionAction.identifier == PATIENT_DOCUMENT_TYPE_IDENTIFIER) {
            updatedBody = updatePatientDocumentInputType(
                chipSelectionValues[PATIENT_DOCUMENT_TYPE_IDENTIFIER]?.id.orEmpty(),
                PATIENT_DOCUMENT_IDENTIFIER,
                updatedBody
            )
        } else if (chipSelectionAction.identifier == RESPONSIBLE_DOCUMENT_TYPE_IDENTIFIER) {
            updatedBody = updatePatientDocumentInputType(
                chipSelectionValues[RESPONSIBLE_DOCUMENT_TYPE_IDENTIFIER]?.id.orEmpty(),
                RESPONSIBLE_DOCUMENT_IDENTIFIER,
                updatedBody
            )
        }

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            )
        )
    }

    private fun updateResponsibleSignature(chipSelectionItemUiModel: ChipSelectionItemUiModel) {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = SIGN_PERSON_CHARGE_W_KEY,
            updater = { model ->
                if (model is SignatureUiModel && model.identifier == SIGN_PERSON_CHARGE_W_KEY) {
                    model.copy(required = chipSelectionItemUiModel.name.equals(NO, true))
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

    private fun updatePatientDocumentInputType(
        id: String,
        identifier: String,
        uiModels: List<BodyRowModel>? = uiState.screenModel?.body
    ) = updateBodyModel(
        uiModels = uiModels,
        updater = { model ->
            if (model is TextFieldUiModel && model.identifier == identifier) {
                model.copy(keyboardOptions = updateKeyboardOptions(id))
            } else {
                model
            }
        }
    )

    private fun updateKeyboardOptions(id: String) = when (id) {
        CC_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Number)
        CE_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        CD_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        PA_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        SC_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        PTP_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        PEP_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        RC_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Number)
        TI_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Number)
        NUIP_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Number)
        ASI_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        MSI_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        TE_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        EP_ID_TYPE -> KeyboardOptions(keyboardType = KeyboardType.Text)
        else -> KeyboardOptions(keyboardType = KeyboardType.Text)
    }

    @Suppress("ComplexMethod", "MagicNumber")
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

    fun handleFiltersAction(filtersAction: GenericUiAction.FiltersAction) {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            updater = { model ->
                if (model is FiltersUiModel) {
                    model.copy(selected = filtersAction.identifier)
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
                        selected = imageButtonAction.itemIdentifier,
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

    private fun updateWithdrawalWitness(withdrawalType: String? = null) {
        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = WITHDRAWAL_DECLARATION_KEY,
            updater = { model ->
                if (model is RichLabelUiModel) {
                    model.copy(
                        text = getWithdrawalWitnessText(
                            patient = fieldsValues.getPatientName(),
                            document = fieldsValues.getPatientDocument(),
                            code = uiState.operationConfig?.vehicleConfig?.typeResource.orEmpty(),
                            withdrawalType = withdrawalType
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

        segmentedSwitchAction.viewsVisibility.forEach { viewVisibility ->
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

    @Suppress("NestedBlockDepth")
    private fun updateComponentVisibility(
        model: BodyRowModel,
        viewsVisibility: Map.Entry<String, Boolean>
    ) = when (model) {
        is ChipOptionsUiModel -> {
            if (viewsVisibility.value) {
                if (model.required) {
                    chipOptionValues[viewsVisibility.key] = model.items.filter {
                        it.selected
                    }.toMutableList()
                }
                model.copy(
                    visibility = viewsVisibility.value,
                    required = true
                )
            } else {
                chipOptionValues.remove(viewsVisibility.key)
                model.copy(
                    items = model.items.map { item -> item.copy(selected = false) },
                    visibility = viewsVisibility.value,
                    required = false
                )
            }
        }

        is ChipSelectionUiModel -> {
            if (viewsVisibility.value) {
                if (model.required) {
                    chipSelectionValues[viewsVisibility.key] = model.items.firstOrNull {
                        it.isSelected
                    } ?: run { ChipSelectionItemUiModel() }
                }
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
                if (model.required) {
                    dropDownValues[viewsVisibility.key] = DropDownInputUiModel(
                        identifier = model.identifier,
                        id = model.selected,
                        fieldValidated = model.selected.isNotEmpty()
                    )
                }
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
                if (model.required) {
                    signatureValues[viewsVisibility.key] = model.signature.orEmpty()
                }
                model.copy(
                    visibility = viewsVisibility.value,
                    required = true
                )
            } else {
                signatureValues.remove(viewsVisibility.key)
                model.copy(
                    signature = null,
                    visibility = viewsVisibility.value,
                    required = false
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
                if (model.required) {
                    fieldsValues[viewsVisibility.key] = InputUiModel(
                        identifier = model.identifier,
                        updatedValue = model.text,
                        required = true
                    )
                }
                model.copy(
                    visibility = viewsVisibility.value,
                    required = true
                )
            } else {
                fieldsValues.remove(viewsVisibility.key)
                model.copy(
                    text = "",
                    visibility = viewsVisibility.value,
                    required = false
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
        val list = values.map {
            when (it.key) {
                TEMPERATURE_KEY -> "${it.key} ${it.value} $TEMPERATURE_SYMBOL"
                GLUCOMETRY_KEY -> "${it.key} ${it.value} $GLUCOMETRY_SYMBOL"
                else -> "${it.key} ${it.value}"
            }
        }

        vitalSignsValues[temporalInfoCard] = values.plus(
            mapOf(
                TIME_KEY to LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern(HOURS_MINUTES_SECONDS_24_HOURS_PATTERN)
                )
            )
        )

        if (temporalInfoCard == INITIAL_VITAL_SIGNS) {
            updateGlasgow()
        }

        val updatedBody = uiState.screenModel?.body?.map { bodyRowModel ->
            if (bodyRowModel is InfoCardUiModel && bodyRowModel.identifier == temporalInfoCard) {
                bodyRowModel.copy(
                    pill = PillUiModel(
                        title = TextUiModel(
                            text = vitalSignsValues[temporalInfoCard]?.get(TIME_KEY).orEmpty(),
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
                    compareByDescending<InfoCardUiModel> {
                        LocalDateTime.parse(
                            it.pill?.title?.text,
                            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
                        )
                    }.thenByDescending { it.title.text }
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
        time = null,
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

    @Suppress("ComplexCondition")
    fun sendMedicalHistory(images: List<File>) {
        uiState = uiState.copy(
            isLoading = true,
            validateFields = true
        )

        val areValidFields = fieldsValues
            .mapValues {
                if (!it.value.fieldValidated) {
                    Timber.d("Field ${it.key} is not validated")
                }
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        val areValidChipOptions = chipOptionValues
            .mapValues {
                it.value.map { chipOption ->
                    if (!chipOption.selected) {
                        Timber.d("ChipOption ${it.key} with ${chipOption.id} is not selected")
                    }
                    chipOption.selected
                }
            }
            .mapValues {
                it.value.contains(true)
            }
            .containsValue(false)
            .not()

        val areValidChipSelections = chipSelectionValues
            .mapValues {
                if (!it.value.isSelected) {
                    Timber.d("ChipSelection ${it.key} is not selected")
                }
                it.value.isSelected
            }
            .containsValue(false)
            .not()

        val areValidDropDowns = dropDownValues
            .mapValues {
                if (!it.value.fieldValidated) {
                    Timber.d("DropDown ${it.key} is not validated")
                }
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        val areValidSignatures = signatureValues
            .mapValues {
                if (it.value.isEmpty()) {
                    Timber.d("Signature ${it.key} is not validated")
                }
                it.value.isNotEmpty()
            }
            .containsValue(false)
            .not()

        val areValidImageButtonSection = imageButtonSectionValues
            .mapValues {
                if (it.value.isEmpty()) {
                    Timber.d("ImageButtonSection ${it.key} is not validated")
                }
                it.value.isNotEmpty()
            }
            .containsValue(false)
            .not()

        if (
            areValidFields &&
            areValidChipOptions &&
            areValidChipSelections &&
            areValidDropDowns &&
            areValidSignatures &&
            areValidImageButtonSection
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
                    chipOptionsValues = chipOptionValues.mapValues {
                        it.value.map { chipOption -> chipOption.id }
                    },
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
            add(savedUri.toString())
        }

        uiState = uiState.copy(
            selectedMediaUris = updatedSelectedMedia,
            navigationModel = MedicalHistoryNavigationModel(
                photoTaken = true
            )
        )
    }

    fun updateMediaActions(selectedMedia: List<String>? = null) {
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

    fun removeMediaActionsImage(selectedMediaIndex: Int) {
        val updatedSelectedMedia = buildList {
            addAll(uiState.selectedMediaUris)

            removeAt(selectedMediaIndex)
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
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
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
