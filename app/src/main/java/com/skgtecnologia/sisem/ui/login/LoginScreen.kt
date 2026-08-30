package com.skgtecnologia.sisem.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.model.toLegalContentModel
import com.skgtecnologia.sisem.ui.login.legal.LegalContent
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.FooterUiAction
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
import com.valkiria.uicomponents.R as UiR

@Suppress("LongMethod")
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onBiometricLogin: (loggedOutRole: String, targetRole: String) -> Unit = { _, _ -> },
    onNavigation: (loginNavigationModel: LoginNavigationModel) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // Biometric is never used while typing a password, so hide its button while the
    // keyboard is up. This frees the vertical space above the keyboard for the form and
    // its INGRESAR button, which otherwise gets clipped (the button is fixed-height and
    // pinned to the bottom).
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigateToBiometric -> {
                    viewModel.consumeBiometricNavigationEvent()
                    onBiometricLogin(uiState.loggedOutRole.orEmpty(), uiState.targetRole.orEmpty())
                }

                uiState.navigationModel != null &&
                    uiState.warning == null -> {
                    viewModel.consumeNavigationEvent()
                    onNavigation(checkNotNull(uiState.navigationModel))
                }
            }
        }
    }

    ConstraintLayout(
        // The body is pinned between the header and the biometric button, so insetting it
        // for the keyboard eats its fixed height instead of scrolling. Inset the whole
        // layout so the button rises above the keyboard and the body keeps the rest.
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        val (header, body, biometric) = createRefs()

        LoginHeaderSection(
            modifier = modifier.constrainAs(header) { top.linkTo(parent.top) }
        )

        BodySection(
            body = uiState.screenModel?.body,
            modifier = Modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(if (imeVisible) parent.bottom else biometric.top)
                    height = Dimension.fillToConstraints
                },
            validateFields = uiState.validateFields,
            applyImePadding = false
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }

        if (!imeVisible) {
            BiometricLoginButton(
                onClick = viewModel::onBiometricLogin,
                modifier = modifier.constrainAs(biometric) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }

    uiState.onLoginLink?.let { link ->
        LaunchedEffect(link) { sheetState.show() }

        BottomSheetView(
            content = { LegalContent(uiModel = link.toLegalContentModel()) },
            sheetState = sheetState,
            scope = scope
        ) {
            viewModel.consumeLoginLinkEvent()
        }
    }

    OnBannerHandler(uiState.warning) {
        // Read the destination before consuming it: uiState is backed by the collected
        // flow, so once consumeNavigationEvent clears it there is nothing left to
        // navigate to and dismissing the expired-password warning strands the user on
        // the login screen (SMA-757).
        val navigationModel = uiState.navigationModel

        viewModel.consumeNavigationEvent()
        viewModel.consumeWarningEvent()
        navigationModel?.let { onNavigation(it) }
    }

    OnBannerHandler(uiState.errorModel) { uiAction ->
        // The duplicate-session banner carries two buttons; every other error banner just
        // has the close icon, which lands here as a dismiss.
        val identifier = (uiAction as? FooterUiAction.FooterButton)?.identifier

        if (identifier == LoginIdentifier.LOGIN_CLOSE_SESSION_CONFIRM.name) {
            viewModel.closeActiveSession()
        } else {
            viewModel.consumeErrorEvent()
        }
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Composable
private fun BiometricLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(bottom = 32.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = UiR.drawable.ic_biometric),
            contentDescription = stringResource(R.string.login_biometric_button),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = stringResource(R.string.login_biometric_button),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun handleAction(
    uiAction: UiAction,
    viewModel: LoginViewModel
) {
    (uiAction as? LoginUiAction)?.let {
        when (uiAction) {
            ForgotPassword -> {
                viewModel.forgotPassword()
            }

            Login -> {
                viewModel.login()
            }

            is LoginPasswordInput -> {
                viewModel.password = uiAction.updatedValue
                viewModel.isValidPassword = uiAction.fieldValidated
            }

            is LoginUserInput -> {
                viewModel.username = uiAction.updatedValue
                viewModel.isValidUsername = uiAction.fieldValidated
            }

            is TermsAndConditions -> {
                viewModel.showLoginLink(
                LoginLink.getLinkByName(link = uiAction.link)
            )
            }
        }
    }
}
