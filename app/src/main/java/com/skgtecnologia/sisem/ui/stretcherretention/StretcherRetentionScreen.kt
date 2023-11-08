package com.skgtecnologia.sisem.ui.stretcherretention

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun StretcherRetentionScreen(
    modifier: Modifier = Modifier,
    onNavigation: (stretcherRetentionNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<StretcherRetentionViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null -> {
                    viewModel.onNavigationHandled()
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
                headerUiModel = it,
                modifier = modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { uiAction ->
                if (uiAction is HeaderUiAction.GoBack) {
                    viewModel.navigateBack()
                }
            }
        }

        BodySection(
            body = uiState.screenModel?.body,
            modifier = modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp),
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) { uiAction ->
        viewModel.handleEvent(uiAction)
    }

    OnBannerHandler(uiModel = uiState.successEvent) {
        viewModel.navigateBack()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: StretcherRetentionViewModel
) {
    when (uiAction) {
        is GenericUiAction.ButtonAction -> viewModel.saveRetention()

        is GenericUiAction.ChipSelectionAction ->
            viewModel.chipSelectionValues[uiAction.identifier] = uiAction.chipSelectionItemUiModel

        is GenericUiAction.InputAction ->
            viewModel.fieldsValues[uiAction.identifier] = InputUiModel(
                uiAction.identifier,
                uiAction.updatedValue,
                uiAction.fieldValidated
            )

        else -> Timber.d("no-op")
    }
}
