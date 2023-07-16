package com.skgtecnologia.sisem.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.skgtecnologia.sisem.domain.login.model.toBottomSheetUiModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetComponent
import com.valkiria.uicomponents.components.loader.LoaderComponent

@Composable
fun LoginScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val uiState = viewModel.uiState

    if (uiState.isLoading) {
        LoaderComponent()
    } else {
        if (uiState.bottomSheetLink != null) {
            BottomSheetComponent(uiModel = uiState.bottomSheetLink.toBottomSheetUiModel())
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
                    .padding(top = 20.dp)
            ) {
                viewModel.showBottomSheet(LoginLink.getLinkByName(it))
            }
        }
    }
}
