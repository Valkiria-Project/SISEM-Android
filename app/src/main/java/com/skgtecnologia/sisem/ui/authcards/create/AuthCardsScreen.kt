package com.skgtecnologia.sisem.ui.authcards.create

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.authcards.create.report.FindingsContent
import com.skgtecnologia.sisem.ui.authcards.create.report.ReportDetailContent
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun AuthCardsScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthCardsViewModel = hiltViewModel<AuthCardsViewModel>(),
    onNavigation: (route: AuthRoute) -> Unit
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    BackHandler {
        Timber.d("Close the App")
        (context as ComponentActivity).finish()
    }

    val notificationsPermissionState: PermissionState? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    val fineLocationPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(notificationsPermissionState?.status) {
        if (notificationsPermissionState?.status?.isGranted == false &&
            !notificationsPermissionState.status.shouldShowRationale
        ) {
            notificationsPermissionState.launchPermissionRequest()
        }

        if (!fineLocationPermissionState.status.isGranted &&
            !fineLocationPermissionState.status.shouldShowRationale
        ) {
            fineLocationPermissionState.launchPermissionRequest()
        }
    }

    if (arePermissionsGranted(notificationsPermissionState, fineLocationPermissionState)) {
        AuthCardsScreenRender(viewModel, modifier, onNavigation)

        OnNotificationHandler(notificationData) {
            notificationData = null
            if (it.isDismiss.not()) {
                // FIXME: Navigate to MapScreen if is type INCIDENT_ASSIGNED
                Timber.d("Navigate to MapScreen")
            }
        }
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.consumeErrorEvent()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Composable
private fun AuthCardsScreenRender(
    viewModel: AuthCardsViewModel,
    modifier: Modifier,
    onNavigation: (route: AuthRoute) -> Unit
) {
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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
            )
        }

        BodySection(
            body = uiState.screenModel?.body,
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
}

private fun arePermissionsGranted(
    notificationsPermissionState: PermissionState?,
    fineLocationPermissionState: PermissionState
): Boolean {
    return if (notificationsPermissionState == null) {
        true
    } else {
        notificationsPermissionState.status.isGranted &&
            fineLocationPermissionState.status.isGranted
    }
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: AuthCardsViewModel,
    onNavigation: (route: AuthRoute) -> Unit
) {
    when (uiAction) {
        is AuthCardsUiAction.AuthCard -> onNavigation(AuthRoute.LoginRoute())

        is GenericUiAction.InfoCardAction -> {
            if (uiAction.isClickCard) {
                onNavigation(AuthRoute.LoginRoute())
                return
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
