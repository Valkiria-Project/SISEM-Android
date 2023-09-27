package com.skgtecnologia.sisem.ui.media

import android.content.ContentResolver
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.model.header.imagesConfirmationHeader
import com.skgtecnologia.sisem.domain.report.model.ImagesConfirmationIdentifier
import com.skgtecnologia.sisem.ui.authcards.report.PagerIndicator
import com.skgtecnologia.sisem.ui.commons.extensions.decodeAsBitmap
import com.skgtecnologia.sisem.ui.commons.extensions.encodeAsBase64
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.report.ReportViewModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.bricks.button.ButtonView
import com.valkiria.uicomponents.components.banner.OnBannerHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.ButtonUiModel
import com.valkiria.uicomponents.model.ui.button.OnClick
import timber.log.Timber
import kotlin.random.Random

@Suppress("LongMethod", "MagicNumber")
@Composable
fun ImagesConfirmationScreen(
    viewModel: ReportViewModel,
    from: String,
    modifier: Modifier = Modifier,
    onNavigation: (imageSelectionNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState
    val contentResolver = LocalContext.current.contentResolver

    LaunchedEffect(uiState) {
        with(uiState) {
            when {
                navigationModel != null && successInfoModel == null && confirmInfoModel == null -> {
                    viewModel.handleNavigation()
                    onNavigation(uiState.navigationModel)
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HeaderSection(
            headerModel = imagesConfirmationHeader(
                titleText = stringResource(id = R.string.images_confirmation_title),
                subtitleText = stringResource(R.string.images_confirmation_subtitle),
                leftIcon = stringResource(R.string.images_confirmation_left_icon)
            )
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                viewModel.goBack()
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
                Timber.d("Eliminar clicked")
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
                if (from == "recordNews") {
                    viewModel.confirmSendReport()
                } else {
                    viewModel.confirmSendFinding()
                }
            }
        }

        val bitmaps = viewModel.uiState.selectedImageUris.map { uri ->
            uri.decodeAsBitmap(LocalContext.current.contentResolver)
        }

        ImagesPager(bitmaps)
    }

    OnBannerHandler(uiState.confirmInfoModel) {
        handleAction(it, from, contentResolver, viewModel)
    }

    OnBannerHandler(uiState.successInfoModel) {
        onNavigation(uiState.navigationModel)
    }

    OnBannerHandler(uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

private fun handleAction(
    uiAction: UiAction,
    from: String,
    contentResolver: ContentResolver,
    viewModel: ReportViewModel
) {
    (uiAction as? FooterUiAction)?.let {
        when (uiAction.identifier) {
            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_CANCEL_BANNER.name ->
                viewModel.handleNavigation()

            ImagesConfirmationIdentifier.IMAGES_CONFIRMATION_SEND_BANNER.name -> {
                val images = viewModel.uiState.selectedImageUris.map { uri ->
                    uri.decodeAsBitmap(contentResolver).encodeAsBase64()
                }

                if (from == "recordNews") {
                    viewModel.sendReport(images)
                } else {
                    viewModel.saveFinding(images)
                }

                viewModel.handleShownConfirm()
            }
        }
    }
}

@Composable
private fun ImagesPager(images: List<Bitmap>) {
    val pageCount = images.size
    Box {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            pageCount
        }

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
