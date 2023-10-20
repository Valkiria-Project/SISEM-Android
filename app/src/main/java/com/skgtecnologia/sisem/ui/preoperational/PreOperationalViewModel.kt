package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.preOperationalConfirmationBanner
import com.skgtecnologia.sisem.domain.model.banner.preOperationalIncompleteFormBanner
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreen
import com.skgtecnologia.sisem.domain.preoperational.usecases.SendPreOperational
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.finding.FindingUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreOperationalViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val getLoginNavigationModel: GetLoginNavigationModel,
    private val getPreOperationalScreen: GetPreOperationalScreen,
    private val sendPreOperational: SendPreOperational
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreOperationalUiState())
        private set

    var temporalFinding by mutableStateOf("")
    var findings = mutableStateMapOf<String, Boolean>()
    var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    var inventoryValues = mutableStateMapOf<String, InputUiModel>()
    var novelties = mutableStateListOf<Novelty>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            getPreOperationalScreen.invoke(androidIdProvider.getAndroidId())
                .onSuccess { preOperationalScreenModel ->
                    preOperationalScreenModel.getFormInitialValues()

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = preOperationalScreenModel,
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false,
                        infoEvent = throwable.mapToUi()
                    )
                }
        }
    }

    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is FindingUiModel -> {
                    Timber.d("it's a FindingModel with id ${bodyRowModel.identifier}")
                    val model = bodyRowModel.segmentedSwitchUiModel
                    findings[model.identifier] = model.selected
                }

                is ChipOptionsUiModel -> {
                    Timber.d("it's a ChipOptionsModel with id ${bodyRowModel.identifier}")
                    bodyRowModel.items.forEach { optionUiModel ->
                        findings[optionUiModel.id] = optionUiModel.selected
                    }
                }

                is InventoryCheckUiModel -> {
                    Timber.d("it's a InventoryCheckModel with id ${bodyRowModel.identifier}")
                    bodyRowModel.items.forEach { checkItemUiModel ->
                        inventoryValues[checkItemUiModel.name.identifier] =
                            InputUiModel(bodyRowModel.identifier)
                    }
                }

                is TextFieldUiModel -> {
                    Timber.d("it's a TextFieldModel with id ${bodyRowModel.identifier}")
                    fieldsValues[bodyRowModel.identifier] = InputUiModel(bodyRowModel.identifier)
                }

                else -> Timber.d("no-op")
            }
        }
    }

    fun showFindingForm() {
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is FindingUiModel && it.segmentedSwitchUiModel.identifier == temporalFinding) {
                val temporalFindingModel = it.copy(
                    segmentedSwitchUiModel = it.segmentedSwitchUiModel.copy(selected = false)
                )

                temporalFindingModel
            } else {
                it
            }
        }.orEmpty()

        uiState = uiState.copy(
            screenModel = uiState.screenModel?.copy(
                body = updatedBody
            ),
            navigationModel = PreOpNavigationModel(
                isNewFindingEvent = true,
                findingId = temporalFinding
            )
        )
    }

    fun revertFinding() {
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is FindingUiModel && it.segmentedSwitchUiModel.identifier == temporalFinding) {
                findings[temporalFinding] = true
                temporalFinding = ""
                val temporalFindingModel = it.copy(
                    segmentedSwitchUiModel = it.segmentedSwitchUiModel.copy(selected = true)
                )
                temporalFindingModel
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

    fun savePreOperational() {
        val isValidInventory = inventoryValues
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        val areValidFields = fieldsValues
            .mapValues {
                it.value.fieldValidated
            }
            .containsValue(false)
            .not()

        Timber.d("Inventory is $isValidInventory")
        Timber.d("Fields are $areValidFields")

        val infoEvent = if (isValidInventory && areValidFields) {
            preOperationalConfirmationBanner().mapToUi()
        } else {
            preOperationalIncompleteFormBanner().mapToUi()
        }

        uiState = uiState.copy(
            validateFields = true,
            infoEvent = infoEvent
        )
    }

    fun sendPreOperational() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            sendPreOperational.invoke(
                findings.toMap(),
                inventoryValues.mapValues { it.value.updatedValue.toInt() },
                fieldsValues.mapValues { it.value.updatedValue },
                novelties.toList()
            ).onSuccess {
                getLoginNavigationModel()
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    infoEvent = throwable.mapToUi()
                )
            }
        }
    }

    private suspend fun getLoginNavigationModel() {
        getLoginNavigationModel.invoke()
            .onSuccess { loginNavigationModel ->
                uiState = uiState.copy(
                    navigationModel = PreOpNavigationModel(
                        isTurnCompleteEvent = loginNavigationModel.isTurnComplete
                    ),
                    isLoading = false
                )
            }
            .onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    infoEvent = throwable.mapToUi()
                )
            }
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun consumeInfoEvent() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }
}
