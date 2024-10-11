package com.skgtecnologia.sisem.ui.medicalhistory.vitalsings

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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun VitalSignsScreen(
    modifier: Modifier = Modifier,
    viewModel: VitalSignsViewModel = hiltViewModel(),
    onNavigation: (vitalSignsNavigationModel: VitalSignsNavigationModel) -> Unit
) {
    val uiState = viewModel.uiState

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    LaunchedEffect(uiState.navigationModel) {
        launch {
            with(uiState.navigationModel) {
                if (this?.goBack == true || this?.values != null) {
                    uiState.navigationModel?.let { onNavigation(it) }
                    viewModel.consumeNavigationEvent()
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
                    viewModel.navigateBack()
                }
            }
        }

        BodySection(
            body = uiState.screenModel?.body,
            modifier = modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp),
            validateFields = uiState.validateFields
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }
    }

    OnNotificationHandler(notificationData) {
        notificationData = null
        if (it.isDismiss.not()) {
            // TECH-DEBT: Navigate to MapScreen if is type INCIDENT_ASSIGNED
            Timber.d("Navigate to MapScreen")
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.consumeInfoEvent()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: VitalSignsViewModel
) {
    when (uiAction) {
        is GenericUiAction.InputAction ->
            viewModel.fieldsValues[uiAction.identifier] = InputUiModel(
                uiAction.identifier,
                uiAction.updatedValue,
                uiAction.fieldValidated
            )
        is GenericUiAction.ButtonAction -> viewModel.saveVitalSigns()
        else -> { }
    }
}
