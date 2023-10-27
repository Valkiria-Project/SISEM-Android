package com.skgtecnologia.sisem.ui.authcards

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.skgtecnologia.sisem.ui.authcards.report.FindingsContent
import com.skgtecnologia.sisem.ui.authcards.report.ReportDetailContent
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun AuthCardsScreen(
    modifier: Modifier = Modifier,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    val viewModel = hiltViewModel<AuthCardsViewModel>()
    val uiState = viewModel.uiState

    val notificationsPermissionState: PermissionState? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    val fineLocationPermissionState: PermissionState =
        rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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
                viewModel.handleShownReportBottomSheet()
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
                viewModel.handleShownFindingsBottomSheet()
            }
        }
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
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
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    when (uiAction) {
        is AuthCardsUiAction.AuthCard -> onNavigation(AuthNavigationRoute.LoginScreen)

        is AuthCardsUiAction.AuthCardNews ->
            viewModel.showReportBottomSheet(uiAction.reportUiDetail)

        is AuthCardsUiAction.AuthCardFindings ->
            viewModel.showFindingsBottomSheet(uiAction.chipSectionUiModel)

        is GenericUiAction.InfoCardAction -> onNavigation(AuthNavigationRoute.LoginScreen)

        else -> Timber.d("no-op")
    }
}
