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
import com.valkiria.uicomponents.model.ui.body.ChipOptionsUiModel
import com.valkiria.uicomponents.model.ui.body.FindingUiModel
import com.valkiria.uicomponents.model.ui.body.InventoryCheckUiModel
import com.valkiria.uicomponents.model.ui.body.TextFieldUiModel
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
    var inventoryValues = mutableStateMapOf<String, Int>()
    var inventoryValidated = mutableStateMapOf<String, Boolean>()
    var fieldsValues = mutableStateMapOf<String, String>()
    var fieldsValidated = mutableStateMapOf<String, Boolean>()
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
                        infoModel = throwable.mapToUi()
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
                        inventoryValidated[checkItemUiModel.name.identifier] = false
                    }
                }

                is TextFieldUiModel -> {
                    Timber.d("it's a TextFieldModel with id ${bodyRowModel.identifier}")
                    fieldsValidated[bodyRowModel.identifier] = false
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
                isNewFinding = true,
                role = "Conductor" // FIXME: Think better about this
            )
        )
    }

    fun revertFinding() {
        if (temporalFinding == "14") {
            Timber.d("revertFinding: id $temporalFinding")
        }
        val updatedBody = uiState.screenModel?.body?.map {
            if (it is FindingUiModel && it.segmentedSwitchUiModel.identifier == temporalFinding) {
                findings[temporalFinding] = true
                temporalFinding = ""
                val temporalFindingModel = it.copy(
                    segmentedSwitchUiModel = it.segmentedSwitchUiModel.copy(selected = true)
                )

                Timber.d("revertFinding: id ${temporalFindingModel.segmentedSwitchUiModel.selected}")
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

    fun handleShownFindingForm() {
        uiState = uiState.copy(
            navigationModel = null
        )
    }

    fun validatePreOperational() {
        uiState = uiState.copy(
            validateFields = true
        )

        val isValidInventory = !inventoryValidated.toMap().containsValue(false)
        val areValidFields = !fieldsValidated.toMap().containsValue(false)

        uiState = if (isValidInventory && areValidFields) {
            uiState.copy(
                infoModel = preOperationalConfirmationBanner().mapToUi()
            )
        } else {
            uiState.copy(
                infoModel = preOperationalIncompleteFormBanner().mapToUi()
            )
        }
    }

    fun sendPreOperational() {
        uiState = uiState.copy(
            isLoading = true
        )

        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            sendPreOperational.invoke(
                findings.toMap(),
                inventoryValues.toMap(),
                fieldsValues.toMap(),
                novelties.toList()
            ).onSuccess {
                getLoginNavigationModel()
            }.onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    infoModel = throwable.mapToUi()
                )
            }
        }
    }

    private suspend fun getLoginNavigationModel() {
        getLoginNavigationModel.invoke()
            .onSuccess { loginNavigationModel ->
                uiState = uiState.copy(
                    navigationModel = PreOpNavigationModel(
                        isTurnComplete = loginNavigationModel.isTurnComplete
                    ),
                    isLoading = false
                )
            }
            .onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    infoModel = throwable.mapToUi()
                )
            }
    }

    fun onPreOpHandled() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            infoModel = null
        )
    }
}
