package com.skgtecnologia.sisem.ui.preoperational.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.preOperationalConfirmationBanner
import com.skgtecnologia.sisem.domain.model.banner.preOperationalIncompleteFormBanner
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.operation.usecases.ObserveOperationConfig
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreen
import com.skgtecnologia.sisem.domain.preoperational.usecases.SendPreOperational
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.chip.FiltersUiModel
import com.valkiria.uicomponents.components.finding.FindingUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldStyle
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class PreOperationalViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val getLoginNavigationModel: GetLoginNavigationModel,
    private val getPreOperationalScreen: GetPreOperationalScreen,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val observeOperationConfig: ObserveOperationConfig,
    private val sendPreOperational: SendPreOperational
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreOperationalUiState())
        private set

    private var temporalFinding by mutableStateOf("")

    private var findingValues = mutableStateMapOf<String, Boolean>()
    private var fieldsValues = mutableStateMapOf<String, InputUiModel>()
    private var inventoryValues = mutableStateMapOf<String, InputUiModel>()
    var novelties = mutableStateListOf<Novelty>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            val operationConfig = async {
                observeOperationConfig.invoke()
                    .onSuccess { operationModel ->
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                operationModel = operationModel,
                            )
                        }
                    }
                    .onFailure { throwable ->
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                infoEvent = throwable.mapToUi()
                            )
                        }
                    }
            }

            val screenModel = async {
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
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                infoEvent = throwable.mapToUi()
                            )
                        }
                    }
            }

            awaitAll(operationConfig, screenModel)
        }
    }

    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is ChipOptionsUiModel -> {
                    bodyRowModel.items.forEach { optionUiModel ->
                        findingValues[optionUiModel.id] = optionUiModel.selected
                    }
                }

                is FindingUiModel -> {
                    val model = bodyRowModel.segmentedSwitchUiModel
                    findingValues[model?.identifier.orEmpty()] = model?.selected ?: false
                }

                is InventoryCheckUiModel -> {
                    bodyRowModel.items.forEach { checkItemUiModel ->
                        inventoryValues[checkItemUiModel.name.identifier] =
                            InputUiModel(
                                bodyRowModel.identifier,
                                bodyRowModel.received.text
                            )
                    }
                }

                is TextFieldUiModel -> {
                    val fieldValidated = bodyRowModel.style == TextFieldStyle.DATE_PICKER &&
                        bodyRowModel.text.isNotEmpty()

                    fieldsValues[bodyRowModel.identifier] = InputUiModel(
                        bodyRowModel.identifier,
                        bodyRowModel.text,
                        fieldValidated
                    )
                }
            }
        }
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

    fun handleFindingAction(findingAction: GenericUiAction.FindingAction) {
        findingValues[findingAction.identifier] = findingAction.status

        if (findingAction.status.not()) {
            temporalFinding = findingAction.identifier
            showFindingForm()
        }
    }

    private fun showFindingForm() {
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is FindingUiModel && it.segmentedSwitchUiModel?.identifier == temporalFinding) {
                val temporalFindingModel = it.copy(
                    segmentedSwitchUiModel = it.segmentedSwitchUiModel?.copy(selected = false)
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

    fun handleInputAction(inputAction: GenericUiAction.InputAction) {
        fieldsValues[inputAction.identifier] = InputUiModel(
            inputAction.identifier,
            inputAction.updatedValue,
            inputAction.fieldValidated
        )

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

    fun handleInventoryAction(inventoryAction: GenericUiAction.InventoryAction) {
        inventoryValues[inventoryAction.itemIdentifier] = InputUiModel(
            inventoryAction.itemIdentifier,
            inventoryAction.updatedValue,
            inventoryAction.fieldValidated
        )

        val updatedBody = updateBodyModel(
            uiModels = uiState.screenModel?.body,
            identifier = inventoryAction.identifier,
            updater = { model ->
                if (model is InventoryCheckUiModel) {
                    model.copy(
                        items = model.items.map {
                            if (it.name.identifier == inventoryAction.itemIdentifier) {
                                it.copy(receivedValueText = inventoryAction.updatedValue)
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

    fun revertFinding() {
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is FindingUiModel && it.segmentedSwitchUiModel?.identifier == temporalFinding) {
                findingValues[temporalFinding] = true
                temporalFinding = ""
                val temporalFindingModel = it.copy(
                    segmentedSwitchUiModel = it.segmentedSwitchUiModel?.copy(selected = true)
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
            preOperationalConfirmationBanner(
                uiState.operationModel?.vehicleConfig?.zone.orEmpty()
            ).mapToUi()
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
        job = viewModelScope.launch {
            sendPreOperational.invoke(
                findingValues.toMap(),
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

    fun handleEvent(uiAction: UiAction) {
        consumeInfoEvent()

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

    private fun consumeInfoEvent() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }
}
