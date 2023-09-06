package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.foundation.layout.fillMaxSize
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
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuth
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuthCodeInput
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun DeviceAuthScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onDeviceAuthenticated: () -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = hiltViewModel<DeviceAuthViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onDeviceAuthenticated -> {
                    viewModel.onDeviceAuthHandled()
                    onDeviceAuthenticated()
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body, footer) = createRefs()

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
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp)
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
                handleFooterAction(uiAction, viewModel, onCancel)
            }
        }
    }

    uiState.disassociateInfoModel?.let {
        ErrorBannerComponent(
            uiModel = it,
            onAction = { },
            onFooterAction = { uiAction ->
                handleFooterUiAction(
                    uiAction = uiAction,
                    viewModel = viewModel,
                    onCancel = {}
                )
            }
        )
    }

    OnErrorHandler(
        errorModel = uiState.errorModel,
        onAction = { viewModel.handleShownError() }
    )

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: DeviceAuthViewModel
) {
    when (uiAction) {
        DeviceAuth -> viewModel.associateDevice()

        is DeviceAuthCodeInput -> viewModel.vehicleCode = uiAction.updatedValue

        is GenericUiAction.SegmentedSwitchAction ->
            viewModel.disassociateDeviceState = uiAction.status

        else -> Timber.d("no-op")
    }
}

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: DeviceAuthViewModel,
    onCancel: () -> Unit
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            DeviceAuthIdentifier.DEVICE_AUTH_BUTTON.name -> viewModel.associateDevice()
            DeviceAuthIdentifier.DEVICE_AUTH_CANCEL_BUTTON.name -> onCancel()
        }
    }
}
