package com.skgtecnologia.sisem.ui.forgotpassword

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.forgotpassword.model.ForgotPasswordIdentifier
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.textfield.InputUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    onNavigation: (forgotPasswordNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<ForgotPasswordViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null -> {
                    viewModel.onNavigationHandled()
                    onNavigation(uiState.navigationModel)
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

    OnBannerHandler(uiModel = uiState.successBanner) {
        viewModel.handleShownSuccess()
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

fun handleAction(uiAction: UiAction, viewModel: ForgotPasswordViewModel) {
    when (uiAction) {
        is GenericUiAction.InputAction -> viewModel.emailValue.value = InputUiModel(
            uiAction.identifier,
            uiAction.updatedValue,
            uiAction.fieldValidated
        )

        else -> Timber.d("no-op")
    }
}

fun handleFooterAction(uiAction: UiAction, viewModel: ForgotPasswordViewModel) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            ForgotPasswordIdentifier.FORGOT_PASSWORD_CANCEL_BUTTON.name -> viewModel.cancel()
            ForgotPasswordIdentifier.FORGOT_PASSWORD_SEND_BUTTON.name -> viewModel.sendEmail()
        }
    }
}
