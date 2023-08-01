package com.skgtecnologia.sisem.ui.deviceauth

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.DeviceAuthUiAction
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuth
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuthCodeInput
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch

@Composable
fun DeviceAuthScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<DeviceAuthViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
            handleUiAction(uiAction, viewModel)
        }

        uiState.screenModel?.footer?.let {
            FooterSection(
                footerModel = it,
                modifier = modifier.constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                }
            ) { uiAction ->
                handleFooterUiAction(uiAction, viewModel)
            }
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

private fun handleUiAction(
    uiAction: UiAction,
    viewModel: DeviceAuthViewModel
) {
    (uiAction as? DeviceAuthUiAction)?.let {
        when (uiAction) {
            DeviceAuth -> viewModel.associateDevice()

            is DeviceAuthCodeInput -> viewModel.vehicleCode = uiAction.updatedValue
        }
    }
}

private fun handleFooterUiAction(
    uiAction: UiAction,
    viewModel: DeviceAuthViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            DeviceAuthIdentifier.DEVICE_AUTH_BUTTON.name -> viewModel.associateDevice()
        }
    }
}
