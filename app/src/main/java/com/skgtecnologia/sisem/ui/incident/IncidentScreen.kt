package com.skgtecnologia.sisem.ui.incident

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun IncidentScreen(
    modifier: Modifier = Modifier,
    onNavigation: (incidentNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<IncidentViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null -> {
                    viewModel.consumeNavigationEvent()
                    onNavigation(uiState.navigationModel)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

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
                    viewModel.goBack()
                }
            }
        }

        BodySection(
            body = uiState.screenModel?.body,
            modifier = modifier.constrainAs(body) {
                top.linkTo(header.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.handleEvent(it)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(uiAction: UiAction, viewModel: IncidentViewModel) {
    when (uiAction) {
        is GenericUiAction.InfoCardAction -> {
            when {
                uiAction.isPill -> viewModel.navigateToStretcherRetention(uiAction.identifier)
                uiAction.patient != null -> viewModel.navigateToAphView(uiAction.patient.orEmpty())
            }
        }

        else -> Timber.d("no-op")
    }
}
