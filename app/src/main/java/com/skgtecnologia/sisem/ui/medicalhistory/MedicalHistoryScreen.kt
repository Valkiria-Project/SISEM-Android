package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.medicalhistory.medsselector.model.MedicineModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun MedicalHistoryScreen(
    modifier: Modifier = Modifier,
    vitalSigns: Map<String, String>?,
    medicine: MedicineModel?,
    onNavigation: (medicalHistoryNavigationModel: NavigationModel?) -> Unit
) {

    val viewModel = hiltViewModel<MedicalHistoryViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.navigationModel) {
        Timber.d("MedicalHistoryScreen: LaunchedEffect uiState pass")
        launch {
            with(uiState.navigationModel) {
                if (this?.isInfoCardEvent == true || this?.isMedsSelectorEvent == true) {
                    onNavigation(uiState.navigationModel)
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

    LaunchedEffect(vitalSigns) {
        Timber.d("MedicalHistoryScreen: LaunchedEffect vitalSigns pass")
        launch {
            if (vitalSigns != null) viewModel.updateVitalSignsInfoCard(vitalSigns)
        }
    }

    LaunchedEffect(medicine) {
        Timber.d("MedicalHistoryScreen: LaunchedEffect medicine pass")
        launch {
            if (medicine != null) viewModel.updateMedicineInfoCard(medicine)
        }
    }

    BodySection(body = uiState.screenModel?.body) { uiAction ->
        handleAction(uiAction, viewModel)
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(
    uiAction: UiAction,
    viewModel: MedicalHistoryViewModel
) {
    when (uiAction) {
        is GenericUiAction.ButtonAction -> {}
        is GenericUiAction.ChipOptionAction -> {}
        is GenericUiAction.ChipSelectionAction -> {
            viewModel.chipValues[uiAction.identifier] = uiAction.text
            viewModel.updateGlasgow()
        }
        is GenericUiAction.InfoCardAction -> viewModel.showVitalSignsForm(uiAction.identifier)
        is GenericUiAction.InputAction -> {}
        is GenericUiAction.HumanBodyAction -> {}
        is GenericUiAction.MedsSelectorAction -> {
            viewModel.showMedicineForm(uiAction.identifier)
        }
        is GenericUiAction.SegmentedSwitchAction -> {}
        is GenericUiAction.SliderAction -> {}
        else -> Timber.d("no-op")
    }
}
