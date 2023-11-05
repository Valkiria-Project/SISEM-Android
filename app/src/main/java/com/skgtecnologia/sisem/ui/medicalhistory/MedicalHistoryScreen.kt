package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.commons.extensions.handleEvent
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.GenericUiAction.StepperAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.media.MediaAction.Camera
import com.valkiria.uicomponents.components.media.MediaAction.File
import com.valkiria.uicomponents.components.media.MediaAction.Gallery
import kotlinx.coroutines.launch
import timber.log.Timber

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
        body = uiState.screenModel?.body
    ) { uiAction ->
        handleAction(uiAction, viewModel)
    }

    OnBannerHandler(uiModel = uiState.infoEvent) { uiAction ->
        uiAction.handleEvent()
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Suppress("ComplexMethod", "LongMethod")
fun handleAction(
    uiAction: UiAction,
    viewModel: MedicalHistoryViewModel
) {
    when (uiAction) {
        is GenericUiAction.ChipOptionAction -> viewModel.handleChipOptionAction(uiAction)

        is GenericUiAction.ChipSelectionAction -> viewModel.handleChipSelectionAction(uiAction)

        is GenericUiAction.DropDownAction -> viewModel.handleDropDownAction(uiAction)

        is GenericUiAction.HumanBodyAction -> viewModel.handleHumanBodyAction(uiAction)

        is GenericUiAction.ImageButtonAction -> viewModel.handleImageButtonAction(uiAction)

        is GenericUiAction.InfoCardAction -> viewModel.showVitalSignsForm(uiAction.identifier)

        is GenericUiAction.InputAction -> viewModel.handleInputAction(uiAction)

        is GenericUiAction.MediaItemAction -> when (uiAction.mediaAction) {
            Camera -> viewModel.showCamera()
            is File -> TODO() // FIXME: SMA-161
            is Gallery -> TODO() // FIXME: SMA-161
        }

        is GenericUiAction.MedsSelectorAction -> viewModel.showMedicineForm(uiAction.identifier)

        is GenericUiAction.SegmentedSwitchAction -> viewModel.handleSegmentedSwitchAction(uiAction)

        is GenericUiAction.SignatureAction -> viewModel.showSignaturePad(uiAction.identifier)

        is GenericUiAction.SliderAction -> viewModel.handleSliderAction(uiAction)

        is StepperAction -> viewModel.sendMedicalHistory()

        else -> Timber.d("no-op")
    }
}
