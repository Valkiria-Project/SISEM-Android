package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun MedicalHistoryScreen(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<MedicalHistoryViewModel>()
    val uiState = viewModel.uiState

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
        is GenericUiAction.ButtonAction -> { }
        is GenericUiAction.ChipOptionAction -> { }
        is GenericUiAction.ChipSelectionAction -> { }
        is GenericUiAction.InfoCardAction -> { }
        is GenericUiAction.InputAction -> { }
        is GenericUiAction.HumanBodyAction -> { }
        is GenericUiAction.SegmentedSwitchAction -> { }
        is GenericUiAction.SliderAction -> { }
        else -> Timber.d("no-op")
    }
}