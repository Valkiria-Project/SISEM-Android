package com.skgtecnologia.sisem.ui.signature.init

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
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
import com.skgtecnologia.sisem.domain.signature.model.SignatureIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
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

@Suppress("LongMethod")
@Composable
fun InitSignatureScreen(
    modifier: Modifier = Modifier,
    viewModel: InitSignatureViewModel = hiltViewModel(),
    onNavigation: (initSignatureNavigationModel: InitSignatureNavigationModel) -> Unit
) {
    val uiState = viewModel.uiState

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
        // The body is pinned between the header and the footer, so insetting it for the
        // keyboard eats its fixed height instead of scrolling. Inset the whole layout so
        // the footer rises above the keyboard and the body keeps the rest.
        modifier = modifier
            .fillMaxSize()
            .imePadding()
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
                    viewModel.goBack()
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
            validateFields = uiState.validateFields,
            applyImePadding = false
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterAction(uiAction, viewModel)
            }
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
        viewModel.handleEvent(it)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(uiAction: UiAction, viewModel: InitSignatureViewModel) {
    when (uiAction) {
        is GenericUiAction.InputAction -> viewModel.document.value = InputUiModel(
            identifier = uiAction.identifier,
            updatedValue = uiAction.updatedValue,
            fieldValidated = uiAction.fieldValidated
        )

        else -> Timber.d("no-op")
    }
}

fun handleFooterAction(uiAction: UiAction, viewModel: InitSignatureViewModel) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            SignatureIdentifier.SIGNATURE_REGISTRATION_SEARCH_BUTTON.name -> {
                viewModel.searchDocument()
            }
        }
    }
}
