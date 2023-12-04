package com.skgtecnologia.sisem.ui.medicalhistory.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.navigation.NavigationModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.media.MediaAction
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MedicalHistoryViewScreen(
    modifier: Modifier = Modifier,
    photoTaken: Boolean = false,
    onNavigation: (stretcherRetentionViewNavigationModel: NavigationModel?) -> Unit
) {
    val viewModel = hiltViewModel<MedicalHistoryViewViewModel>()
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

    LaunchedEffect(photoTaken) {
        launch {
            if (photoTaken) {
                viewModel.updateMediaActions()
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (header, body) = createRefs()

        uiState.screenModel?.header?.let {
            HeaderSection(
                headerUiModel = it,
                modifier = modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }

        BodySection(
            body = uiState.screenModel?.body,
            modifier = modifier
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(top = 20.dp)
        ) { uiAction ->
            handleAction(uiAction, viewModel)
        }
    }

    OnBannerHandler(uiModel = uiState.infoEvent) {
        // FIXME: navigate to send email ?
    }

    OnBannerHandler(uiModel = uiState.errorEvent) { uiAction ->
        viewModel.handleEvent(uiAction)
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(uiAction: UiAction, viewModel: MedicalHistoryViewViewModel) {
    when (uiAction) {
        is GenericUiAction.InputAction -> {}

        is GenericUiAction.MediaItemAction -> when (uiAction.mediaAction) {
            MediaAction.Camera -> viewModel.showCamera()
            is MediaAction.MediaFile -> viewModel.updateMediaActions(
                selectedMedia = (uiAction.mediaAction as MediaAction.MediaFile).uris,
            )

            is MediaAction.Gallery -> viewModel.updateMediaActions(
                selectedMedia = (uiAction.mediaAction as MediaAction.Gallery).uris,
            )

            is MediaAction.RemoveFile -> viewModel.removeMediaActionsImage(
                (uiAction.mediaAction as MediaAction.RemoveFile).uri
            )
        }

        is GenericUiAction.StepperAction -> viewModel.sendMedicalHistoryView()

        is HeaderUiAction.GoBack -> viewModel.navigateBack()

        else -> Timber.d("no-op")
    }
}
