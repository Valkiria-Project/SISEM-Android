package com.skgtecnologia.sisem.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.model.toLegalContentModel
import com.skgtecnologia.sisem.ui.login.legal.LegalContent
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.LoginUiAction
import com.valkiria.uicomponents.action.LoginUiAction.ForgotPassword
import com.valkiria.uicomponents.action.LoginUiAction.Login
import com.valkiria.uicomponents.action.LoginUiAction.LoginPasswordInput
import com.valkiria.uicomponents.action.LoginUiAction.LoginUserInput
import com.valkiria.uicomponents.action.LoginUiAction.TermsAndConditions
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.bottomsheet.BottomSheetView
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch

@Suppress("LongMethod")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigation: (loginNavigationModel: LoginNavigationModel) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null && uiState.warning == null -> {
                    viewModel.consumeNavigationEvent()
                    onNavigation(checkNotNull(uiState.navigationModel))
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

        BottomSheetView(
            content = { LegalContent(uiModel = link.toLegalContentModel()) },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.consumeLoginLinkEvent()
        }
    }

    OnBannerHandler(uiState.warning) {
        viewModel.consumeNavigationEvent()
        viewModel.consumeWarningEvent()
        uiState.navigationModel?.let { navigationModel -> onNavigation(navigationModel) }
    }

    OnBannerHandler(uiState.errorModel) {
        viewModel.consumeErrorEvent()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: LoginViewModel
) {
    (uiAction as? LoginUiAction)?.let {
        when (uiAction) {
            ForgotPassword -> viewModel.forgotPassword()

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
