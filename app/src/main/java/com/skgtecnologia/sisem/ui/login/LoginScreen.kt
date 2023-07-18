package com.skgtecnologia.sisem.ui.login

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
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.model.toBottomSheetUiModel
import com.skgtecnologia.sisem.ui.error.toBottomSheetUiModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.action.LoginUiAction.ForgotPassword
import com.valkiria.uicomponents.action.LoginUiAction.Login
import com.valkiria.uicomponents.action.LoginUiAction.LoginPasswordInput
import com.valkiria.uicomponents.action.LoginUiAction.LoginUserInput
import com.valkiria.uicomponents.action.LoginUiAction.TermsAndConditions
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun LoginScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (uiState.isLoading) {
        LoaderComponent() // FIXME: This is causing the screen to be re rendered = state loss
    } else {
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
                    .padding(top = 20.dp)
            ) { uiAction ->
                handleUiAction(uiAction, viewModel)
            }
        }

        uiState.bottomSheetLink?.let { link ->
            scope.launch {
                sheetState.show()
            }

            BottomSheetComponent(
                uiModel = link.toBottomSheetUiModel(),
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

            BottomSheetComponent(
                uiModel = errorUiModel.toBottomSheetUiModel(),
                sheetState = sheetState,
                scope = scope
            ) {
                viewModel.handleShownBottomSheet()
            }
        }
    }
}

private fun handleUiAction(
    uiAction: UiAction,
    viewModel: LoginViewModel
) {
    when (uiAction) {
        // FIXME: Navigate to ForgotPasswordScreen
        ForgotPassword -> Timber.d("ForgotPasswordButton clicked")

        Login -> viewModel.login()

        is LoginUserInput -> viewModel.username = uiAction.updatedValue

        is LoginPasswordInput -> viewModel.password = uiAction.updatedValue

        is TermsAndConditions -> viewModel.showBottomSheet(
            LoginLink.getLinkByName(link = uiAction.link)
        )
    }
}
