package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.changepassword.usecases.GetLoginNavigationModel
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.model.body.ChipOptionsModel
import com.skgtecnologia.sisem.domain.model.body.FindingModel
import com.skgtecnologia.sisem.domain.model.body.InventoryCheckModel
import com.skgtecnologia.sisem.domain.model.body.TextFieldModel
import com.skgtecnologia.sisem.domain.model.footer.preOperationalAuxiliaryFooterModel
import com.skgtecnologia.sisem.domain.model.footer.preOperationalDriverFooterModel
import com.skgtecnologia.sisem.domain.model.footer.preOperationalMedicFooterModel
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreen
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetRole
import com.skgtecnologia.sisem.domain.preoperational.usecases.SendPreOperational
import com.skgtecnologia.sisem.ui.navigation.model.PreOpNavigationModel
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel
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
    private val getRole: GetRole,
    private val sendPreOperational: SendPreOperational
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreOperationalUiState())
        private set

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
                    val role = getRole.invoke().getOrThrow()

                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            screenModel = preOperationalScreenModel.copy(
                                body = buildList {
                                    addAll(preOperationalScreenModel.body)

                                    when (role) {
                                        OperationRole.AUXILIARY_AND_OR_TAPH ->
                                            add(preOperationalAuxiliaryFooterModel())

                                        OperationRole.DRIVER ->
                                            add(preOperationalDriverFooterModel())

                                        OperationRole.MEDIC_APH ->
                                            add(preOperationalMedicFooterModel())

                                        else -> Timber.d("no-op")
                                    }

                                }
                            ),
                            isLoading = false
                        )
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
        }
    }

    private fun ScreenModel.getFormInitialValues() {
        this.body.forEach { bodyRowModel ->
            when (bodyRowModel) {
                is FindingModel -> {
                    Timber.d("it's a FindingModel with id ${bodyRowModel.identifier}")
                    val model = bodyRowModel.segmentedSwitchModel
                    findings[model.identifier] = model.selected
                }

                is ChipOptionsModel -> {
                    Timber.d("it's a ChipOptionsModel with id ${bodyRowModel.identifier}")
                    bodyRowModel.items.forEach { optionUiModel ->
                        findings[optionUiModel.id] = optionUiModel.selected
                    }
                }

                is InventoryCheckModel -> {
                    Timber.d("it's a InventoryCheckModel with id ${bodyRowModel.identifier}")
                    bodyRowModel.items.forEach { checkItemUiModel ->
                        inventoryValidated[checkItemUiModel.name.identifier] = false
                    }
                }

                is TextFieldModel -> {
                    Timber.d("it's a TextFieldModel with id ${bodyRowModel.identifier}")
                    fieldsValidated[bodyRowModel.identifier] = false
                }

                else -> Timber.d("no-op")
            }
        }
    }

    fun showFindingForm() {
        uiState = uiState.copy(
            navigationModel = PreOpNavigationModel(
                isNewFinding = true,
                role = "Conductor" // FIXME: Think better about this
            )
        )
    }

    fun handleShownFindingForm() {
        uiState = uiState.copy(
            navigationModel = null
        )
    }

    fun savePreOperational() {
        uiState = uiState.copy(
            validateFields = true
        )

        val isValidInventory = !inventoryValidated.toMap().containsValue(false)
        val areValidFields = !fieldsValidated.toMap().containsValue(false)

        if (isValidInventory && areValidFields) {
            sendPreOperational()
        } else {
            // FIXME: Show Banner with message about fields
            uiState = uiState.copy(
                errorModel = BannerUiModel(
                    icon = "ic_alert",
                    title = "Incompleto",
                    description = "Para guardar el preoperacional es necesario que complete" +
                            " todos los campos."
                )
            )
        }
    }

    private fun sendPreOperational() {
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
                    errorModel = throwable.mapToUi()
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
                    errorModel = throwable.mapToUi()
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
            errorModel = null
        )
    }
}
