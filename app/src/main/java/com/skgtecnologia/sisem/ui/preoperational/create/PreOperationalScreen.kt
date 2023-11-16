package com.skgtecnologia.sisem.ui.preoperational.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalIdentifier
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun PreOperationalScreen(
    modifier: Modifier = Modifier,
    novelty: Novelty?,
    revertFinding: Boolean?,
    onNavigation: (preOpNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<PreOperationalViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.navigationModel) {
        Timber.d("PreOperationalScreen: LaunchedEffect navigationModel pass")
        launch {
            with(uiState.navigationModel) {
                if (this?.isNewFindingEvent == true || this?.isTurnCompleteEvent != null) {
                    onNavigation(uiState.navigationModel)
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

    LaunchedEffect(novelty, revertFinding) {
        Timber.d("PreOperationalScreen: LaunchedEffect novelty and revertFinding pass")
        launch {
            when {
                novelty != null -> viewModel.novelties.add(novelty)
                revertFinding == true -> viewModel.revertFinding()
            }
        }
    }

    BodySection(
        body = uiState.screenModel?.body,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        validateFields = uiState.validateFields
    ) { uiAction ->
        handleBodyAction(uiAction, viewModel)
    }

    OnBannerHandler(uiModel = uiState.infoEvent) { uiAction ->
        handleFooterAction(uiAction, viewModel)
        viewModel.consumeInfoEvent()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleBodyAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    when (uiAction) {
        is GenericUiAction.ButtonAction -> viewModel.savePreOperational()

        is GenericUiAction.FindingAction -> viewModel.handleFindingAction(uiAction)

        is GenericUiAction.InputAction -> viewModel.handleInputAction(uiAction)

        is GenericUiAction.InventoryAction -> viewModel.handleInventoryAction(uiAction)

        else -> Timber.d("no-op")
    }
}

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        if (it.identifier == PreOperationalIdentifier.PREOP_SAVE_BUTTON.name) {
            viewModel.sendPreOperational()
        }
    }
}
