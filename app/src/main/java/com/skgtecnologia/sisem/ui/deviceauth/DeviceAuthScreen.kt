package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuthCodeInput
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun DeviceAuthScreen(
    modifier: Modifier = Modifier,
    viewModel: DeviceAuthViewModel = hiltViewModel(),
    onNavigation: (deviceAuthNavigationModel: DeviceAuthNavigationModel) -> Unit
) {
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
            )
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

    OnBannerHandler(uiModel = uiState.disassociateInfoModel) {
        handleFooterAction(
            uiAction = it,
            viewModel = viewModel
        )
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleEvent(it)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: DeviceAuthViewModel
) {
    when (uiAction) {
        is DeviceAuthCodeInput -> {
            viewModel.vehicleCode = uiAction.updatedValue
            viewModel.isValidVehicleCode = uiAction.fieldValidated
        }

        is GenericUiAction.SegmentedSwitchAction -> {
            viewModel.disassociateDeviceState = uiAction.status == false
        }

        else -> {
            Timber.d("no-op")
        }
    }
}

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: DeviceAuthViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            DeviceAuthIdentifier.DEVICE_AUTH_BUTTON.name -> viewModel.associateDevice()
            DeviceAuthIdentifier.DEVICE_AUTH_CANCEL_BUTTON.name -> viewModel.cancel()
            DeviceAuthIdentifier.DEVICE_AUTH_CONTINUE_BANNER.name -> viewModel.cancel()
            DeviceAuthIdentifier.DEVICE_AUTH_CANCEL_BANNER.name -> viewModel.cancelBanner()
        }
    }
}
