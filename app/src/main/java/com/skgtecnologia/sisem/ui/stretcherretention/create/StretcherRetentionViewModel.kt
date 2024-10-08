package com.skgtecnologia.sisem.ui.stretcherretention.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.auth.usecases.LogoutCurrentUser
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.banner.stretcherRetentionSuccess
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.GetStretcherRetentionScreen
import com.skgtecnologia.sisem.domain.stretcherretention.usecases.SaveStretcherRetention
import com.skgtecnologia.sisem.ui.commons.extensions.handleAuthorizationErrorEvent
import com.skgtecnologia.sisem.ui.commons.extensions.updateBodyModel
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

private const val KEY_REASON_IDENTIFIER = "KEY_REASON"
private const val KEY_WHICH_IDENTIFIER = "KEY_WHICH"
private const val KEY_INFORMED_IDENTIFIER = "KEY_INFORMED"
private const val KEY_VIA_RADIO_IDENTIFIER = "KEY_VIA_RADIO"
private const val OTHERS = "OTHERS"
private const val TRUE = "true"

@Suppress("TooManyFunctions")
@HiltViewModel
class StretcherRetentionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logoutCurrentUser: LogoutCurrentUser,
    private val getStretcherRetentionScreen: GetStretcherRetentionScreen,
    private val saveStretcherRetention: SaveStretcherRetention
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(StretcherRetentionUiState())
        private set

    private val idAph = savedStateHandle.toRoute<MainRoute.StretcherRetentionRoute>().idAph

    private var chipOptionValues = mutableStateMapOf<String, MutableList<ChipOptionUiModel>>()
    private var chipSelectionValues = mutableStateMapOf<String, ChipSelectionItemUiModel>()
    private var fieldsValues = mutableStateMapOf<String, InputUiModel>()

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getStretcherRetentionScreen.invoke(idAph = idAph)
                .onSuccess { stretcherRetentionScreen ->
                    stretcherRetentionScreen.getFormInitialValues()

                    val updatedBody = updateReasonSelection(
                        uiModels = stretcherRetentionScreen.body
                    )

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = stretcherRetentionScreen.copy(
                                body = updatedBody
                            ),
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
    }

    private fun updateReasonSelection(
        uiModels: List<BodyRowModel>? = uiState.screenModel?.body
    ): List<BodyRowModel> {
        return uiModels.orEmpty().map { model ->
            if (model is TextFieldUiModel && model.identifier == KEY_WHICH_IDENTIFIER) {
                val chipSelection = uiModels
                    ?.filterIsInstance<ChipSelectionUiModel>()
                    ?.find { it.identifier == KEY_REASON_IDENTIFIER && it.selected == OTHERS }

                model.copy(visibility = chipSelection != null)
            } else if (
                model is ChipSelectionUiModel && model.identifier == KEY_VIA_RADIO_IDENTIFIER
            ) {
                val chipSelection = uiModels
                    ?.filterIsInstance<ChipSelectionUiModel>()
                    ?.find { it.identifier == KEY_INFORMED_IDENTIFIER && it.selected == TRUE }

                model.copy(visibility = chipSelection != null)
            } else {
                model
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
                chipOption.removeIf { it.id == chipOptionAction.chipOptionUiModel.id }

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

    fun handleInputAction(inputAction: GenericUiAction.InputAction) {
        fieldsValues[inputAction.identifier] = InputUiModel(
            inputAction.identifier,
            inputAction.updatedValue,
            inputAction.fieldValidated,
            inputAction.required
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

    fun saveRetention() {
        uiState = uiState.copy(
            validateFields = true
        )

        if (fieldsValues.values.all { it.fieldValidated }) {
            uiState = uiState.copy(
                isLoading = true
            )

            job?.cancel()
            job = viewModelScope.launch {
                saveStretcherRetention.invoke(
                    idAph = idAph,
                    fieldsValue = fieldsValues.mapValues { it.value.updatedValue },
                    chipSelectionValues = chipSelectionValues.mapValues { it.value.id }
                ).onSuccess {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            successEvent = stretcherRetentionSuccess().mapToUi(),
                        )
                    }
                }.onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            isLoading = false,
                            infoEvent = throwable.mapToUi()
                        )
                    }
                }
            }
        }
    }

    fun handleEvent(uiAction: UiAction) {
        consumeShownError()

        uiAction.handleAuthorizationErrorEvent {
            job?.cancel()
            job = viewModelScope.launch {
                logoutCurrentUser.invoke()
                    .onSuccess { username ->
                        UnauthorizedEventHandler.publishUnauthorizedEvent(username)
                    }.onFailure {
                        UnauthorizedEventHandler.publishUnauthorizedEvent(it.toString())
                    }
            }
        }
    }

    fun consumeNavigationEvent() {
        uiState = uiState.copy(
            isLoading = false,
            validateFields = false,
            navigationModel = null
        )
    }

    private fun consumeShownError() {
        uiState = uiState.copy(
            infoEvent = null
        )
    }

    fun gpBack() {
        uiState = uiState.copy(
            successEvent = null,
            navigationModel = StretcherRetentionNavigationModel(back = true)
        )
    }

    @Suppress("LongMethod", "NestedBlockDepth")
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
}
