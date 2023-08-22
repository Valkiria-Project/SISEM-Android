package com.skgtecnologia.sisem.ui.preoperational

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalIdentifier
import com.skgtecnologia.sisem.ui.bottomsheet.FindingFormContent
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.PreOperationalUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.CoroutineScope
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

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onFindingForm -> {
                    scope.launch {
                        sheetState.show()
                    }
                }

                uiState.preOpNavigationModel != null -> {
                    viewModel.onFindingFormImagesHandled()
                    onNavigation(uiState.preOpNavigationModel)
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
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp)
        ) { uiAction ->
            handleBodyAction(uiAction, viewModel)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterAction(uiAction, viewModel)
            }
        }
    }

    OnFindingForm(uiState, viewModel, sheetState, scope)
    OnError(uiState, viewModel)
    OnLoading(uiState, modifier)
}

private fun handleBodyAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    (uiAction as? PreOperationalUiAction)?.let {
        when (uiAction) {
            is PreOperationalUiAction.DriverVehicleKMInput -> {
                // FIXME
                Timber.d("Handle DriverVehicleKMInput ${uiAction.updatedValue}")
            }

            is PreOperationalUiAction.PreOpSwitchState -> {
                if (uiAction.status.not()) {
                    viewModel.showFindingForm()
                }
            }
        }
    }
}

@Composable
private fun OnFindingForm(
    uiState: PreOperationalUiState,
    viewModel: PreOperationalViewModel,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    if (uiState.onFindingForm) {
        BottomSheetComponent(
            content = {
                FindingFormContent { uiAction ->
                    handleFindingFormContentAction(uiAction, viewModel)
                }
            },
            sheetState = sheetState,
            scope = scope
        ) {
            // FIXME: Use dismissed instead of shown to revert the selection
            viewModel.handleShownFindingForm()
        }
    }
}

private fun handleFindingFormContentAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    viewModel.handleShownFindingForm()
    (uiAction as GenericUiAction.ButtonAction).let { buttonAction ->
        when (buttonAction.identifier) {
            PreOperationalIdentifier.FINDING_FORM_IMAGE_BUTTON.name ->
                viewModel.onFindingFormImages()

            PreOperationalIdentifier.FINDING_FORM_SAVE_BUTTON.name ->
                Timber.d("FIXME") // FIXME: Finish this
        }
    }
}

@Composable
private fun OnError(
    uiState: PreOperationalUiState,
    viewModel: PreOperationalViewModel
) {
    uiState.errorModel?.let { errorUiModel ->
        ErrorBannerComponent(
            uiModel = errorUiModel
        ) {
            viewModel.handleShownError()
        }
    }
}

@Composable
private fun OnLoading(
    uiState: PreOperationalUiState,
    modifier: Modifier
) {
    if (uiState.isLoading) {
        HideKeyboard()
        LoaderComponent(modifier)
    }
}

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            PreOperationalIdentifier.DRIVER_PREOP_SAVE_BUTTON.name -> viewModel.sendPreOperational()
        }
    }
}
