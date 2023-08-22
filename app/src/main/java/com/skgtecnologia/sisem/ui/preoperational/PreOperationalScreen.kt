package com.skgtecnologia.sisem.ui.preoperational

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.PreOperationalUiAction
import com.valkiria.uicomponents.action.PreOperationalUiAction.SavePreOperational
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun PreOperationalScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
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
            handleUiAction(uiAction, viewModel)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterUiAction(uiAction, viewModel)
            }
        }
    }

    if (uiState.onFindingForm) {
        BottomSheetComponent(
            content = {
                FindingFormContent()
            },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownFindingForm()
        }
    }

    uiState.errorModel?.let { errorUiModel ->
        ErrorBannerComponent(
            uiModel = errorUiModel
        ) {
            viewModel.handleShownError()
        }
    }

    if (uiState.isLoading) {
        HideKeyboard()
        LoaderComponent(modifier)
    }
}

private fun handleUiAction(
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

            SavePreOperational -> viewModel.sendPreOperational()
        }
    }
}

private fun handleFooterUiAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            PreOperationalIdentifier.DRIVER_PREOP_SAVE_BUTTON.name -> viewModel.sendPreOperational()
        }
    }
}
