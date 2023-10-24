package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.dropdown.DropDownInputUiModel
import com.valkiria.uicomponents.components.stepper.StepperComponent
import com.valkiria.uicomponents.components.stepper.StepperUiModel
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun MedicalHistoryScreen(
    modifier: Modifier = Modifier,
    vitalSigns: Map<String, String>?,
    medicine: Map<String, String>?,
    signature: String?,
    onNavigation: (medicalHistoryNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<MedicalHistoryViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.navigationModel) {
        launch {
            uiState.navigationModel?.let {
                onNavigation(uiState.navigationModel)
                viewModel.consumeNavigationEvent()
            }
        }
    }

    LaunchedEffect(vitalSigns) {
        launch {
            vitalSigns?.let { viewModel.updateVitalSignsInfoCard(vitalSigns) }
        }
    }

    LaunchedEffect(medicine) {
        launch {
            medicine?.let { viewModel.updateMedicineInfoCard(medicine) }
        }
    }

    LaunchedEffect(signature) {
        launch {
            signature?.let { viewModel.updateSignature(signature) }
        }
    }

    BodySection(
        body = uiState.screenModel?.body,
        stickyFooterContent = {
            val stepperUiModel = uiState.screenModel?.body
                ?.filterIsInstance<StepperUiModel>()
                ?.firstOrNull()

            stepperUiModel?.let {
                StepperComponent(it) {
                    Timber.d("Stepper action")
                    // FIXME: Hardcode for now Api call
                    viewModel.sendMedicalHistory()
                }
            }
        }
    ) { uiAction ->
        handleAction(uiAction, viewModel)
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Suppress("ComplexMethod")
fun handleAction(
    uiAction: UiAction,
    viewModel: MedicalHistoryViewModel
) {
    when (uiAction) {
        is GenericUiAction.ChipOptionAction -> {
            val chipOption = viewModel.chipOptionValues[uiAction.identifier]

            when {
                chipOption != null && chipOption.contains(uiAction.chipOptionUiModel.id) -> {
                    chipOption.remove(uiAction.chipOptionUiModel.id)

                    if (chipOption.isEmpty()) viewModel.chipOptionValues.remove(uiAction.identifier)
                }

                chipOption != null && chipOption.contains(uiAction.chipOptionUiModel.id).not() -> {
                    chipOption.add(uiAction.chipOptionUiModel.id)
                }

                else -> viewModel.chipOptionValues[uiAction.identifier] =
                    mutableListOf(uiAction.chipOptionUiModel.id)
            }
        }

        is GenericUiAction.ChipSelectionAction -> {
            viewModel.chipSelectionValues[uiAction.identifier] = uiAction.chipSelectionItemUiModel
            viewModel.updateGlasgow()
        }

        is GenericUiAction.DropDownAction ->
            viewModel.dropDownValues[uiAction.identifier] = DropDownInputUiModel(
                uiAction.identifier,
                uiAction.id,
                uiAction.name,
                uiAction.fieldValidated
            )

        is GenericUiAction.HumanBodyAction -> {
            val humanBody = viewModel.humanBodyUiValues.find { it.area == uiAction.values.area }

            if (humanBody != null) {
                viewModel.humanBodyUiValues.remove(humanBody)
            } else {
                viewModel.humanBodyUiValues.add(uiAction.values)
            }
        }

        is GenericUiAction.ImageButtonAction ->
            viewModel.imageButtonSectionValues[uiAction.identifier] = uiAction.identifier

        is GenericUiAction.InfoCardAction -> viewModel.showVitalSignsForm(uiAction.identifier)

        is GenericUiAction.InputAction -> {
            viewModel.fieldsValues[uiAction.identifier] = InputUiModel(
                uiAction.identifier,
                uiAction.updatedValue,
                uiAction.fieldValidated
            )
            viewModel.updateFurAndGestationWeeks()
        }

        is GenericUiAction.MedsSelectorAction ->
            viewModel.showMedicineForm(uiAction.identifier)

        is GenericUiAction.SegmentedSwitchAction ->
            viewModel.segmentedValues[uiAction.identifier] = uiAction.status

        is GenericUiAction.SignatureAction -> viewModel.showSignaturePad(uiAction.identifier)

        is GenericUiAction.SliderAction ->
            viewModel.sliderValues[uiAction.identifier] = uiAction.value.toString()

        else -> Timber.d("no-op")
    }
}
