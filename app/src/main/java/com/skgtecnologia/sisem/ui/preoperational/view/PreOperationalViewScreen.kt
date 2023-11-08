package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun PreOperationalViewScreen(
    modifier: Modifier = Modifier,
    onNavigation: (preOpByRoleNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<PreOperationalViewViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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

    BodySection(
        body = uiState.screenModel?.body,
        modifier = modifier.fillMaxSize()
    ) { uiAction ->
        handleAction(uiAction, viewModel)
    }

    uiState.findingDetail?.let {
        scope.launch { sheetState.show() }

        BottomSheetView(
            content = { FindingDetailContent(model = uiState.findingDetail) },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownFindingBottomSheet()
        }
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleEvent(it)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewViewModel
) {
    when (uiAction) {
        is GenericUiAction.FindingAction -> viewModel.showFindings(uiAction.findingDetail)

        is HeaderUiAction.GoBack -> viewModel.goBack()

        else -> Timber.d("no-op")
    }
}
