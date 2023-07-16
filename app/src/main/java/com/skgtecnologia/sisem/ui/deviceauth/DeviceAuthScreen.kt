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
import com.valkiria.uicomponents.components.loader.LoaderComponent
import timber.log.Timber

@Composable
fun DeviceAuthScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val deviceAuthViewModel = hiltViewModel<DeviceAuthViewModel>()
    val uiState = deviceAuthViewModel.uiState

    if (uiState.isLoading) {
        LoaderComponent()
    } else {
        ConstraintLayout(
            modifier = modifier.fillMaxSize()
        ) {
            val (header, body, footer) = createRefs()

            /*DeviceAuthHeaderSection(
                modifier = modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                }
            )*/

            BodySection(
                body = uiState.screenModel?.body,
                modifier = modifier
                    .constrainAs(body) {
                        top.linkTo(header.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .padding(top = 20.dp)
            ) {
                Timber.d("This is going to be fun")
            }
        }
    }
}
