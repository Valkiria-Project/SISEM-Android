package com.skgtecnologia.sisem.ui.deviceauth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.FooterSection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.components.loader.LoaderComponent

@Composable
fun DeviceAuthScreen(
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    val deviceAuthViewModel = hiltViewModel<DeviceAuthViewModel>()
    val uiState = deviceAuthViewModel.uiState

    if (uiState.isLoading) { // FIXME: Do same as LoginScreen
        LoaderComponent()
    } else {
        ConstraintLayout(
            modifier = modifier.fillMaxSize()
        ) {
            with(uiState.screenModel) {
                val (header, body, footer) = createRefs()

                this?.header?.let {
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
                    body = this?.body,
                    isTablet = isTablet,
                    modifier = modifier
                        .constrainAs(body) {
                            top.linkTo(header.bottom)
                            bottom.linkTo(footer.top)
                            height = Dimension.fillToConstraints
                        }
                        .padding(top = 20.dp)
                )

                this?.footer?.let {
                    FooterSection(
                        footerModel = it,
                        modifier = modifier.constrainAs(footer) {
                            bottom.linkTo(parent.bottom)
                        }
                    )
                }
            }
        }
    }
}
