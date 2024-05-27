package com.skgtecnologia.sisem.ui.medicalhistory.view

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.sections.BodySection
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.GenericUiAction.FiltersAction
import com.valkiria.uicomponents.action.GenericUiAction.InputAction
import com.valkiria.uicomponents.action.GenericUiAction.MediaItemAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.media.MediaAction
import com.valkiria.uicomponents.extensions.handleMediaUris
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun MedicalHistoryViewScreen(
    modifier: Modifier = Modifier,
    photoTaken: Boolean = false,
    onNavigation: (medicalHistoryViewNavigationModel: MedicalHistoryViewNavigationModel) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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
            ) { uiAction ->
                if (uiAction is HeaderUiAction.GoBack) {
                    viewModel.goBack()
                }
            }
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
            handleAction(uiAction, viewModel, context, scope)
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

private fun handleAction(
    uiAction: UiAction,
    viewModel: MedicalHistoryViewViewModel,
    context: Context,
    scope: CoroutineScope
) {
    when (uiAction) {
        is FiltersAction -> viewModel.handleFiltersAction(uiAction)

        is InputAction -> {}

        is MediaItemAction -> when (uiAction.mediaAction) {
            MediaAction.Camera -> viewModel.showCamera()
            is MediaAction.MediaFile -> scope.launch {
                val uris = (uiAction.mediaAction as MediaAction.MediaFile).uris
                val mediaItems = context.handleMediaUris(
                    uris,
                    viewModel.uiState.operationConfig?.maxFileSizeKb
                )

                viewModel.updateMediaActions(
                    mediaItems = mediaItems
                )
            }

            is MediaAction.Gallery -> scope.launch {
                val uris = (uiAction.mediaAction as MediaAction.Gallery).uris
                val mediaItems = context.handleMediaUris(
                    uris,
                    viewModel.uiState.operationConfig?.maxFileSizeKb
                )

                viewModel.updateMediaActions(
                    mediaItems = mediaItems
                )
            }

            is MediaAction.RemoveFile -> viewModel.removeMediaActionsImage(
                (uiAction.mediaAction as MediaAction.RemoveFile).index
            )
        }

        is GenericUiAction.StepperAction -> scope.launch {
            val images = viewModel.uiState.selectedMediaItems.mapNotNull { it.file }
            val description = viewModel.uiState.description
            viewModel.sendMedicalHistoryView(images, description)
        }

        else -> Timber.d("no-op")
    }
}
