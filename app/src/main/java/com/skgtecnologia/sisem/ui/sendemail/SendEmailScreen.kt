package com.skgtecnologia.sisem.ui.sendemail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.domain.model.header.sendEmailHeader
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SendEmailScreen(
    modifier: Modifier = Modifier,
    onNavigation: (sendEmailNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<SendEmailViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        launch {
            when {
                uiState.navigationModel != null -> {
                    viewModel.consumeNavigationEvent()
                    onNavigation(uiState.navigationModel)
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

        HeaderSection(
            headerUiModel = sendEmailHeader(),
            modifier = modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) { uiAction ->
            when (uiAction) {
                is HeaderUiAction.GoBack -> {
                    viewModel.cancel()
                }

                is HeaderUiAction.RightAction -> {
                    viewModel.sendEmail()
                }

                else -> Timber.d("no-op")
            }
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        viewModel.navigateToMain()
    }

    OnBannerHandler(uiModel = uiState.errorEvent) { uiAction ->
        viewModel.handleEvent(uiAction)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}
