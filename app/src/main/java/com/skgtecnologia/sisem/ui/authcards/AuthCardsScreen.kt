package com.skgtecnologia.sisem.ui.authcards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.model.bricks.mapToDomain
import com.skgtecnologia.sisem.ui.bottomsheet.FindingsContent
import com.skgtecnologia.sisem.ui.bottomsheet.ReportDetailContent
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import kotlinx.coroutines.launch

@Composable
fun AuthCardsScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    val viewModel = hiltViewModel<AuthCardsViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
            modifier = modifier.constrainAs(body) {
                top.linkTo(header.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
        ) { uiAction ->
            handleAction(uiAction, viewModel, onNavigation)
        }
    }

    uiState.reportDetail?.let {
        scope.launch { sheetState.show() }

        BottomSheetComponent(
            content = { ReportDetailContent(model = uiState.reportDetail) },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownReportBottomSheet()
        }
    }

    uiState.chipSection?.let {
        scope.launch { sheetState.show() }

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

    OnErrorHandler(errorModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(
    uiAction: UiAction,
    viewModel: AuthCardsViewModel,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    (uiAction as? AuthCardsUiAction)?.let {
        when (uiAction) {
            AuthCardsUiAction.AuthCard -> onNavigation(AuthNavigationRoute.LoginScreen)

            is AuthCardsUiAction.AuthCardNews ->
                viewModel.showReportBottomSheet(uiAction.reportDetail.mapToDomain())

            is AuthCardsUiAction.AuthCardFindings ->
                viewModel.showFindingsBottomSheet(uiAction.chipSectionUiModel.mapToDomain())
        }
    }
}
