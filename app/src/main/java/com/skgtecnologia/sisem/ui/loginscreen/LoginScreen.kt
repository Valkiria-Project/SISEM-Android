package com.skgtecnologia.sisem.ui.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
        modifier = modifier.fillMaxSize().background(color = Color(0xFF242426))
    ) {
        val (header, body) = createRefs()

        LoginHeaderSection(
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top, margin = 10.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
            }
        )

        BodySection(
            body = uiState.loginScreenModel?.body,
            modifier = modifier.background(color = Color(0xFF242426)).constrainAs(body) {
                top.linkTo(header.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}
