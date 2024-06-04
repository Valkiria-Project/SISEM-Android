package com.skgtecnologia.sisem.ui.authcards.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.authcards.create.report.FindingsContent
import com.skgtecnologia.sisem.ui.authcards.create.report.ReportDetailContent
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun AuthCardViewScreen(
    modifier: Modifier = Modifier,
    onNavigation: (authCardViewNavigationModel: AuthCardViewNavigationModel) -> Unit
) {
    val viewModel = hiltViewModel<AuthCardViewViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

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

    uiState.reportDetail?.let {
        scope.launch { sheetState.show() }

        BottomSheetView(
            content = { ReportDetailContent(model = uiState.reportDetail) },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.consumeReportBottomSheetEvent()
        }
    }

    uiState.chipSection?.let {
        scope.launch { sheetState.show() }

        BottomSheetView(
            content = {
                FindingsContent(chipSection = uiState.chipSection)
            },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.consumeFindingsBottomSheetEvent()
        }
    }

    OnNotificationHandler(notificationData) {
        notificationData = null
        if (it.isDismiss.not()) {
            // FIXME: Navigate to MapScreen if is type INCIDENT_ASSIGNED
            Timber.d("Navigate to MapScreen")
        }
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleEvent(it)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(uiAction: UiAction, viewModel: AuthCardViewViewModel) {
    when (uiAction) {
        is GenericUiAction.InfoCardAction -> {
            if (uiAction.isClickCard) {
                viewModel.navigate(uiAction.identifier)
            }

            uiAction.reportDetail?.let {
                viewModel.showReportBottomSheet(it)
            }

            uiAction.chipSection?.let {
                viewModel.showFindingsBottomSheet(it)
            }
        }

        else -> Timber.d("no-op")
    }
}
