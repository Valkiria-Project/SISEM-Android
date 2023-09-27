package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.FindingModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.banner.OnBannerHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun PreOperationalScreen(
    modifier: Modifier = Modifier,
    revertFinding: Boolean?,
    onNavigation: (preOpNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<PreOperationalViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel?.isNewFinding == true -> {
                    viewModel.handleShownFindingForm()
                    onNavigation(uiState.navigationModel)
                }

                uiState.navigationModel?.isTurnComplete != null -> {
                    viewModel.onPreOpHandled()
                    onNavigation(uiState.navigationModel)
                }
            }
        }
    }

    BodySection(
        body = if (revertFinding == true) {
            handleFindingRevert(viewModel)
        } else {
            uiState.screenModel?.body
        },
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        validateFields = uiState.validateFields
    ) { uiAction ->
        handleBodyAction(uiAction, viewModel)
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleFindingRevert(
    viewModel: PreOperationalViewModel
): List<BodyRowModel>? {
    return viewModel.uiState.screenModel?.body?.map { model ->
        if (model is FindingModel &&
            model.segmentedSwitchModel.identifier == viewModel.temporalFinding
        ) {
            val revertedFindingModel = model.copy(
                segmentedSwitchModel = model.segmentedSwitchModel.copy(selected = true)
            )
            viewModel.revertTemporalFinding()
            revertedFindingModel
        } else {
            model
        }
    }
}

private fun handleBodyAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    when (uiAction) {
        is GenericUiAction.ButtonAction -> viewModel.savePreOperational()

        is GenericUiAction.ChipOptionAction ->
            viewModel.findings[uiAction.identifier] = uiAction.status

        is GenericUiAction.FindingAction -> {
            viewModel.findings[uiAction.identifier] = uiAction.status

            if (uiAction.status.not()) {
                viewModel.temporalFinding = uiAction.identifier
                viewModel.showFindingForm()
            }
        }

        is GenericUiAction.InputAction -> {
            viewModel.fieldsValues[uiAction.identifier] = uiAction.updatedValue
            viewModel.fieldsValidated[uiAction.identifier] = uiAction.fieldValidated
        }

        is GenericUiAction.InventoryAction -> {
            uiAction.updatedValue.toIntOrNull()?.let { checkItemValue ->
                viewModel.inventoryValues[uiAction.identifier] = checkItemValue
                viewModel.inventoryValidated[uiAction.identifier] = uiAction.fieldValidated
            }
        }

        else -> Timber.d("no-op")
    }
}
