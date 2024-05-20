package com.skgtecnologia.sisem.ui.report.media

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.imagesConfirmationHeader
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.skgtecnologia.sisem.ui.authcards.create.report.PagerIndicator
import com.skgtecnologia.sisem.ui.navigation.REPORT
import com.skgtecnologia.sisem.ui.report.ReportNavigationModel
import com.skgtecnologia.sisem.ui.report.ReportViewModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.button.ButtonView
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.extensions.decodeAsBitmap
import com.valkiria.uicomponents.extensions.storeUriAsFileToCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.random.Random

@Suppress("LongMethod", "MagicNumber")
@androidx.compose.foundation.ExperimentalFoundationApi
@Composable
fun ImagesConfirmationScreen(
    viewModel: ReportViewModel,
    from: String,
    modifier: Modifier = Modifier,
    onNavigation: (imageSelectionNavigationModel: ReportNavigationModel) -> Unit
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        viewModel.uiState.selectedImageUris.size
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            Timber.d("Page change", "Page changed to $page")
            viewModel.currentImage = page
        }
    }

    LaunchedEffect(uiState) {
        launch {
            with(uiState) {
                if (navigationModel != null && successInfoModel == null &&
                    confirmInfoModel == null
                ) {
                    uiState.navigationModel?.let { onNavigation(it) }
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

    LaunchedEffect(uiState.selectedImageUris) {
        launch {
            if (uiState.selectedImageUris.isEmpty()) {
                viewModel.navigateBackFromImages()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderSection(
            headerUiModel = imagesConfirmationHeader(
                titleText = stringResource(id = R.string.images_confirmation_title),
                subtitleText = stringResource(R.string.images_confirmation_subtitle),
                leftIcon = stringResource(R.string.back_icon)
            )
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                viewModel.navigateBackFromImages()
            }
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ButtonView(
                uiModel = ButtonUiModel(
                    identifier = Random(100).toString(),
                    label = stringResource(R.string.images_confirmation_delete_image_cta),
                    textStyle = TextStyle.BUTTON_2,
                    style = ButtonStyle.TRANSPARENT,
                    overrideColor = MaterialTheme.colorScheme.primary,
                    onClick = OnClick.DISMISS,
                    size = ButtonSize.DEFAULT,
                    arrangement = Arrangement.Start,
                    modifier = Modifier
                )
            ) {
                viewModel.removeCurrentImage()
            }

            ButtonView(
                uiModel = ButtonUiModel(
                    identifier = Random(100).toString(),
                    label = stringResource(R.string.images_confirmation_confirm_images_cta),
                    textStyle = TextStyle.BUTTON_2,
                    style = ButtonStyle.TRANSPARENT,
                    overrideColor = MaterialTheme.colorScheme.primary,
                    onClick = OnClick.DISMISS,
                    size = ButtonSize.DEFAULT,
                    arrangement = Arrangement.Start,
                    modifier = Modifier
                )
            ) {
                if (from == REPORT) {
                    viewModel.saveReportImages()
                } else {
                    viewModel.saveFindingImages()
                }
            }
        }

        val bitmaps = viewModel.uiState.selectedImageUris.map { uri ->
            uri.toUri().decodeAsBitmap(LocalContext.current.contentResolver)
        }

        ImagesPager(pagerState = pagerState, images = bitmaps)
    }

    OnBannerHandler(uiState.confirmInfoModel) {
        handleAction(it, from, context, viewModel, scope)
    }

    OnBannerHandler(uiState.successInfoModel) {
        uiState.navigationModel?.let { it1 -> onNavigation(it1) }
    }

    OnBannerHandler(uiState.infoEvent) {
        viewModel.consumeShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    from: String,
    context: Context,
    viewModel: ReportViewModel,
    coroutineScope: CoroutineScope
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_CANCEL_BANNER.name ->
                viewModel.consumeNavigationEvent()

            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_SEND_BANNER.name -> {
                coroutineScope.launch {
                    val images = viewModel.uiState.selectedImageUris.mapNotNull { uri ->
                        runCatching {
                            context.storeUriAsFileToCache(
                                uri.toUri()
                            )
                        }.fold(
                            onSuccess = { it },
                            onFailure = { null }
                        )
                    }

                    if (from == REPORT) {
                        viewModel.confirmReportImages(images)
                    } else {
                        viewModel.confirmFindingImages(images)
                    }

                    viewModel.consumeShownConfirm()
                }
            }

            else -> Timber.d("no-op")
        }
    }
}

@Composable
private fun ImagesPager(pagerState: PagerState, images: List<Bitmap>) {
    val pageCount = images.size
    Box {
        HorizontalPager(state = pagerState) { index ->
            Image(
                bitmap = images[index].asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        PagerIndicator(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            pageCount = pageCount,
            currentPage = pagerState.currentPage
        )
    }
}
