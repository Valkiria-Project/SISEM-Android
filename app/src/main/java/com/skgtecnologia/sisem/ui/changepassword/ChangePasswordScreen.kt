package com.skgtecnologia.sisem.ui.changepassword

import HideKeyboard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.changepassword.model.ChangePasswordIdentifier
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.ChangePasswordUiAction
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: () -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = hiltViewModel<ChangePasswordViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onChangePassword -> {
                    viewModel.onChangePasswordHandled()
                    onNavigation() // FIXME
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
                .padding(top = 20.dp),
            validateFields = uiState.validateFields
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
                handleFooterUiAction(uiAction, viewModel, onCancel)
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
    viewModel: ChangePasswordViewModel
) {
    (uiAction as? ChangePasswordUiAction)?.let {
        when(uiAction) {
            is ChangePasswordUiAction.ConfirmPasswordInput -> {
                viewModel.confirmNewPassword = uiAction.updatedValue
                viewModel.isValidConfirmNewPassword = uiAction.fieldValidated
            }
            is ChangePasswordUiAction.NewPasswordInput -> {
                viewModel.newPassword = uiAction.updatedValue
                viewModel.isValidNewPassword = uiAction.fieldValidated
            }
            is ChangePasswordUiAction.OldPasswordInput -> {
                viewModel.oldPassword = uiAction.updatedValue
            }
        }
    }
}

private fun handleFooterUiAction(
    uiAction: UiAction,
    viewModel: ChangePasswordViewModel,
    onCancel: () -> Unit
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            ChangePasswordIdentifier.CHANGE_PASSWORD_CANCEL_BUTTON.name -> onCancel()
            ChangePasswordIdentifier.CHANGE_PASSWORD_SEND_BUTTON.name -> viewModel.change()
        }
    }
}
