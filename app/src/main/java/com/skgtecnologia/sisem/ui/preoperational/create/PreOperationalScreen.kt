package com.skgtecnologia.sisem.ui.preoperational.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.preoperational.model.PreOperationalIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongParameterList")
@Composable
fun PreOperationalScreen(
    modifier: Modifier = Modifier,
    viewModel: PreOperationalViewModel = hiltViewModel(),
    novelty: Novelty?,
    revertFinding: Boolean?,
    onNavigation: (preOpNavigationModel: PreOpNavigationModel) -> Unit
) {
    val uiState = viewModel.uiState

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    LaunchedEffect(uiState.navigationModel) {
        Timber.d("PreOperationalScreen: LaunchedEffect navigationModel pass")
        launch {
            with(uiState.navigationModel) {
                if (this?.isNewFinding == true || this?.isTurnComplete != null) {
                    uiState.navigationModel?.let { onNavigation(it) }
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

    LaunchedEffect(novelty, revertFinding) {
        Timber.d("PreOperationalScreen: LaunchedEffect novelty and revertFinding pass")
        launch {
            when {
                novelty != null -> viewModel.novelties.add(novelty)
                revertFinding == true -> viewModel.revertFinding()
            }
        }
    }

    PreOperationalScreenRender(viewModel, modifier)

    OnNotificationHandler(notificationData) {
        notificationData = null
        if (it.isDismiss.not()) {
            // TECH-DEBT: Navigate to MapScreen if is type INCIDENT_ASSIGNED
            Timber.d("Navigate to MapScreen")
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) { uiAction ->
        handleFooterAction(uiAction, viewModel)
        viewModel.handleEvent(uiAction)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Composable
private fun PreOperationalScreenRender(
    viewModel: PreOperationalViewModel,
    modifier: Modifier
) {
    val uiState = viewModel.uiState

    BodySection(
        body = uiState.screenModel?.body,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        validateFields = uiState.validateFields
    ) { uiAction ->
        handleBodyAction(uiAction, viewModel)
    }
}

private fun handleBodyAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    when (uiAction) {
        is GenericUiAction.ButtonAction -> viewModel.savePreOperational()

        is GenericUiAction.FiltersAction -> viewModel.handleFiltersAction(uiAction)

        is GenericUiAction.FindingAction -> viewModel.handleFindingAction(uiAction)

        is GenericUiAction.InputAction -> viewModel.handleInputAction(uiAction)

        is GenericUiAction.InventoryAction -> viewModel.handleInventoryAction(uiAction)

        else -> Timber.d("no-op")
    }
}

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: PreOperationalViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        if (it.identifier == PreOperationalIdentifier.PREOP_SAVE_BUTTON.name) {
            viewModel.sendPreOperational()
        }
    }
}
