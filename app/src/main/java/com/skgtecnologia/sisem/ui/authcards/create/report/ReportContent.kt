package com.skgtecnologia.sisem.ui.authcards.create.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.banner.report.ReportDetailUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.extensions.decodeAsBase64Bitmap

@androidx.compose.foundation.ExperimentalFoundationApi
@Composable
fun ReportContent(
    model: ReportDetailUiModel
) {
    val imagesBitmap = model.images.map { image ->
        image.decodeAsBase64Bitmap()
    }

    val pageCount = imagesBitmap.size
    Column(modifier = Modifier.padding(top = 20.dp)) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            pageCount
        }
        HorizontalPager(state = pagerState) { index ->
            Image(
                bitmap = imagesBitmap[index].asImageBitmap(),
                contentDescription = model.title.text,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
        PagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            pageCount = pageCount,
            currentPage = pagerState.currentPage
        )

        ReportTitle(model)

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = model.subtitle.text,
            style = model.subtitle.textStyle.toTextStyle()
        )
    }
}

@Composable
private fun ReportTitle(model: ReportDetailUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = model.title.text,
            style = model.title.textStyle.toTextStyle()
        )

        Icon(
            modifier = Modifier.padding(start = 16.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_clock),
            contentDescription = "Clock icon"
        )

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = model.description.text,
            style = model.description.textStyle.toTextStyle()
        )
    }
}

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    selectedColor: Color = Color.White,
    inactiveColor: Color = selectedColor.copy(alpha = 0.66f)
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) { iteration ->
            val color = if (currentPage == iteration) selectedColor else inactiveColor
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(if (currentPage == iteration) 8.dp else 6.dp)
            )
        }
    }
}
