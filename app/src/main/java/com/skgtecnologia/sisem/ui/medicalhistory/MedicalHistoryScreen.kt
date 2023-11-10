package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.GenericUiAction.StepperAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.media.MediaAction.Camera
import com.valkiria.uicomponents.components.media.MediaAction.Gallery
import com.valkiria.uicomponents.components.media.MediaAction.MediaFile
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongParameterList")
@Composable
fun MedicalHistoryScreen(
    viewModel: MedicalHistoryViewModel,
    modifier: Modifier = Modifier,
    vitalSigns: Map<String, String>?,
    medicine: Map<String, String>?,
    signature: String?,
    photoTaken: Boolean?,
    onNavigation: (medicalHistoryNavigationModel: NavigationModel?) -> Unit
) {
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

    LaunchedEffect(photoTaken) {
        launch {
            photoTaken?.let { viewModel.updateMediaActions() }
        }
    }

    BodySection(
        body = uiState.screenModel?.body
    ) { uiAction ->
        handleAction(uiAction, viewModel)
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.consumeShownInfoEvent()
    }

    OnBannerHandler(uiModel = uiState.errorEvent) { uiAction ->
        viewModel.handleEvent(uiAction)
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

        is HeaderUiAction.GoBack -> viewModel.goBack()

        is GenericUiAction.HumanBodyAction -> viewModel.handleHumanBodyAction(uiAction)

        is GenericUiAction.ImageButtonAction -> viewModel.handleImageButtonAction(uiAction)

        is GenericUiAction.InfoCardAction -> viewModel.showVitalSignsForm(uiAction.identifier)

        is GenericUiAction.InputAction -> viewModel.handleInputAction(uiAction)

        is GenericUiAction.MediaItemAction -> when (uiAction.mediaAction) {
            Camera -> viewModel.showCamera()
            is MediaFile -> viewModel.updateMediaActions(
                selectedMedia = (uiAction.mediaAction as MediaFile).uris,
            )
            is Gallery -> viewModel.updateMediaActions(
                selectedMedia = (uiAction.mediaAction as Gallery).uris,
            )
        }

        is GenericUiAction.MedsSelectorAction -> viewModel.showMedicineForm(uiAction.identifier)

        is GenericUiAction.SegmentedSwitchAction -> viewModel.handleSegmentedSwitchAction(uiAction)

        is GenericUiAction.SignatureAction -> viewModel.showSignaturePad(uiAction.identifier)

        is GenericUiAction.SliderAction -> viewModel.handleSliderAction(uiAction)

        is StepperAction -> viewModel.sendMedicalHistory()

        else -> Timber.d("no-op")
    }
}
