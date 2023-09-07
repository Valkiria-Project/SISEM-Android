package com.skgtecnologia.sisem.ui.preoperational

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.banner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun PreOperationalScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
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

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body, footer) = createRefs()

        uiState.screenModel?.header?.let {
            HeaderSection(
                headerModel = it,
                modifier = modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }

        BodySection(
            body = uiState.screenModel?.body,
            isTablet = isTablet,
            modifier = modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp),
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleBodyAction(uiAction, viewModel)
        }
    }

    OnErrorHandler(uiModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleBodyAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    when (uiAction) {
        is FooterUiAction.FooterButton -> viewModel.savePreOperational()

        is GenericUiAction.ChipOptionAction ->
            viewModel.findings[uiAction.identifier] = uiAction.status

        is GenericUiAction.FindingAction -> {
            viewModel.findings[uiAction.identifier] = uiAction.status

            if (uiAction.status.not()) {
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
