package com.skgtecnologia.sisem.ui.media

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.skgtecnologia.sisem.ui.bottomsheet.PagerIndicator
import com.skgtecnologia.sisem.ui.commons.extensions.decodeAsBitmap
import com.skgtecnologia.sisem.ui.commons.extensions.encodeAsBase64
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.report.ReportViewModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.bricks.button.ButtonView
import com.valkiria.uicomponents.components.errorbanner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.OnLoadingHandler
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TabletWidth
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.ButtonUiModel
import com.valkiria.uicomponents.model.ui.button.OnClick
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun ImagesConfirmationScreen(
    viewModel: ReportViewModel,
    from: String,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (imageSelectionNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState
    val contentResolver = LocalContext.current.contentResolver

    LaunchedEffect(uiState) {
        when {
            uiState.navigationModel != null && uiState.successInfoModel == null-> {
                viewModel.handleNavigation()
                onNavigation(uiState.navigationModel)
            }
        }
    }

    Column(
        modifier = if (isTablet) {
            modifier.width(TabletWidth)
        } else {
            modifier.fillMaxWidth()
        },
    ) {
        HeaderSection(
            headerModel = getImagesConfirmationHeaderModel()
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
                    val images = viewModel.uiState.selectedImageUris.map { uri ->
                        uri.decodeAsBitmap(contentResolver).encodeAsBase64()
                    }
                    viewModel.sendRecordNews(images)
                } else {
                    // TODO
                }
            }
        }

        val bitmaps = viewModel.uiState.selectedImageUris.map { uri ->
            uri.decodeAsBitmap(LocalContext.current.contentResolver)
        }

        ImagesPager(bitmaps)
    }

    uiState.successInfoModel?.let { infoUiModel ->
        OnErrorHandler(infoUiModel) {
            onNavigation(uiState.navigationModel)
        }
    }

    OnErrorHandler(uiState.errorModel) {
        viewModel.handleShownError()
    }

    OnLoadingHandler(uiState.isLoading, modifier)
}

@Composable
private fun getImagesConfirmationHeaderModel() = HeaderModel(
    title = TextModel(
        stringResource(id = R.string.images_confirmation_title),
        TextStyle.HEADLINE_1
    ),
    subtitle = TextModel(
        stringResource(R.string.images_confirmation_subtitle),
        TextStyle.HEADLINE_5
    ),
    leftIcon = stringResource(R.string.images_confirmation_left_icon),
    modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
)

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
