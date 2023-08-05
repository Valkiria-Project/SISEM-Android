package com.skgtecnologia.sisem.ui.authcards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.AuthNavigationRoute
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.UiAction

@Suppress("UnusedPrivateMember")
@Composable
fun AuthCardsScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    val viewModel = hiltViewModel<AuthCardsViewModel>()
    val uiState = viewModel.uiState

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

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
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        ) { uiAction ->
            handleUiAction(uiAction, onNavigation)
        }
    }
}

fun handleUiAction(
    uiAction: UiAction,
    onNavigation: (route: AuthNavigationRoute) -> Unit
) {
    (uiAction as? AuthCardsUiAction)?.let {
        when (uiAction) {
            AuthCardsUiAction.AuthCard -> onNavigation(AuthNavigationRoute.Login)
            AuthCardsUiAction.AuthCardNews -> {} // FIXME:
        }
    }
}
