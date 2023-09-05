package com.skgtecnologia.sisem.ui.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel
import com.skgtecnologia.sisem.ui.report.ReportViewModel
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.model.props.TabletWidth
import com.valkiria.uicomponents.model.props.TextStyle

@Composable
fun ImagesConfirmationScreen(
    viewModel: ReportViewModel,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onNavigation: (imageSelectionNavigationModel: NavigationModel?) -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(uiState) {
        when {
            uiState.navigationModel != null -> {
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

        val bitmaps = viewModel.uiState.selectedImageUris.map {
            val inputStream = LocalContext.current.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap
        }

        ImagesPager(bitmaps)
    }
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
    Box(modifier = Modifier.padding(top = 20.dp)) {
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
