package com.skgtecnologia.sisem.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.sections.body.BodySection

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val loginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
    val uiState = loginScreenViewModel.uiState

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
            body = uiState.loginScreenModel?.body,
            modifier = modifier.constrainAs(body) {
                top.linkTo(header.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }
        )
    }
}
