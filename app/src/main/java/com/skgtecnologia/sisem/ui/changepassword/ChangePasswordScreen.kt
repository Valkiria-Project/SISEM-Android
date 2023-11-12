package com.skgtecnologia.sisem.ui.changepassword

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.changepassword.model.ChangePasswordIdentifier
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.ChangePasswordUiAction
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    onNavigation: (loginNavigationModel: NavigationModel?) -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = hiltViewModel<ChangePasswordViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onCancel -> {
                    viewModel.consumeCancelEvent()
                    onCancel()
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
            validateFields = uiState.validateFields
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

    OnBannerHandler(uiModel = uiState.successInfoModel) {
        viewModel.consumeChangePasswordEvent()
        onNavigation(uiState.loginNavigationModel)
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.consumeErrorEvent()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: ChangePasswordViewModel
) {
    (uiAction as? ChangePasswordUiAction)?.let {
        when (uiAction) {
            is ChangePasswordUiAction.ConfirmPasswordInput -> {
                viewModel.confirmedNewPassword = uiAction.updatedValue
                viewModel.isValidConfirmedNewPassword = uiAction.fieldValidated
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

private fun handleFooterAction(
    uiAction: UiAction,
    viewModel: ChangePasswordViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            ChangePasswordIdentifier.CHANGE_PASSWORD_CANCEL_BUTTON.name -> viewModel.cancel()
            ChangePasswordIdentifier.CHANGE_PASSWORD_SAVE_BUTTON.name -> viewModel.change()
        }
    }
}
