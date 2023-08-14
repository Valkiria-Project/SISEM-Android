package com.skgtecnologia.sisem.ui.authcards

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.model.bricks.mapToDomain
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@Composable
fun AuthCardsScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    val viewModel = hiltViewModel<AuthCardsViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

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
        ) { uiAction ->
            handleUiAction(uiAction, viewModel, onNavigation)
        }
    }

    uiState.reportDetail?.let {
        scope.launch {
            sheetState.show()
        }

        BottomSheetComponent(
            content = {
                ReportDetailContent(model = uiState.reportDetail)
            },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownReportBottomSheet()
        }
    }

    uiState.chipSection?.let {
        scope.launch {
            sheetState.show()
        }

        BottomSheetComponent(
            content = {
                FindingsContent(chipSection = uiState.chipSection)
            },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownFindingsBottomSheet()
        }
    }

    uiState.errorModel?.let { errorUiModel ->
        scope.launch {
            sheetState.show()
        }

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

fun handleUiAction(
    uiAction: UiAction,
    viewModel: AuthCardsViewModel,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    (uiAction as? AuthCardsUiAction)?.let {
        when (uiAction) {
            AuthCardsUiAction.AuthCard -> onNavigation(AuthNavigationRoute.Login)
            is AuthCardsUiAction.AuthCardNews ->
                viewModel.showReportBottomSheet(uiAction.reportDetail.mapToDomain())
            is AuthCardsUiAction.AuthCardFindings ->
                viewModel.showFindingsBottomSheet(uiAction.chipSectionUiModel.mapToDomain())
        }
    }
}
