package com.skgtecnologia.sisem.ui.login

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
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.model.toLegalContentModel
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.LoginUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.errorbanner.ErrorBannerComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun LoginScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.onLogin -> {
                    viewModel.onLoginHandled()
                    onNavigation(AuthNavigationRoute.DeviceAuth)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

        LoginHeaderSection(
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top)
            }
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
            handleUiAction(uiAction, viewModel)
        }
    }

    uiState.bottomSheetLink?.let { link ->
        scope.launch {
            sheetState.show()
        }

        BottomSheetComponent(
            content = {
                LegalContent(uiModel = link.toLegalContentModel())
            },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.handleShownBottomSheet()
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
    viewModel: LoginViewModel
) {
    (uiAction as? LoginUiAction)?.let {
        when (uiAction) {
            LoginUiAction.ForgotPassword -> {
                // FIXME: Navigate to ForgotPasswordScreen
                Timber.d("ForgotPasswordButton clicked")
            }

            LoginUiAction.Login -> viewModel.login()

            is LoginUiAction.LoginPasswordInput -> {
                viewModel.password = uiAction.updatedValue
                viewModel.isValidPassword = uiAction.fieldValidated
            }

            is LoginUiAction.LoginUserInput -> {
                viewModel.username = uiAction.updatedValue
                viewModel.isValidUsername = uiAction.fieldValidated
            }

            is LoginUiAction.TermsAndConditions -> viewModel.showBottomSheet(
                LoginLink.getLinkByName(link = uiAction.link)
            )
        }
    }
}
