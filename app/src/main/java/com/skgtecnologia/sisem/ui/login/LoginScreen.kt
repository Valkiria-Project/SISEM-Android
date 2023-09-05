package com.skgtecnologia.sisem.ui.login

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
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.model.toLegalContentModel
import com.skgtecnologia.sisem.ui.bottomsheet.LegalContent
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.LoginUiAction
import com.valkiria.uicomponents.action.LoginUiAction.ForgotPassword
import com.valkiria.uicomponents.action.LoginUiAction.Login
import com.valkiria.uicomponents.action.LoginUiAction.LoginPasswordInput
import com.valkiria.uicomponents.action.LoginUiAction.LoginUserInput
import com.valkiria.uicomponents.action.LoginUiAction.TermsAndConditions
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (loginNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onLogin -> {
                    viewModel.onLoginHandled()
                    onNavigation(uiState.loginNavigationModel)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

        LoginHeaderSection(
            modifier = modifier.constrainAs(header) { top.linkTo(parent.top) }
        )

        BodySection(
            body = uiState.screenModel?.body,
            isTablet = isTablet,
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

    uiState.onLoginLink?.let { link ->
        scope.launch { sheetState.show() }

        BottomSheetComponent(
            content = { LegalContent(uiModel = link.toLegalContentModel()) },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownLoginLink()
        }
    }

    uiState.warning?.let { errorUiModel ->
        ErrorBannerComponent(uiModel = errorUiModel) {
            viewModel.onLoginHandled()
            viewModel.handleShownWarning()
            onNavigation(uiState.loginNavigationModel)
        }
    }

    OnErrorHandler(uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: LoginViewModel
) {
    (uiAction as? LoginUiAction)?.let {
        when (uiAction) {
            ForgotPassword -> {
                // FIXME: Navigate to ForgotPasswordScreen
                Timber.d("ForgotPasswordButton clicked")
            }

            Login -> viewModel.login()

            is LoginPasswordInput -> {
                viewModel.password = uiAction.updatedValue
                viewModel.isValidPassword = uiAction.fieldValidated
            }

            is LoginUserInput -> {
                viewModel.username = uiAction.updatedValue
                viewModel.isValidUsername = uiAction.fieldValidated
            }

            is TermsAndConditions -> viewModel.showLoginLink(
                LoginLink.getLinkByName(link = uiAction.link)
            )
        }
    }
}
