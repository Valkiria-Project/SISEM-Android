package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.ui.authcards.create.report.PagerIndicator
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.bricks.banner.finding.FindingDetailUiModel
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.extensions.decodeAsBase64Bitmap

@Composable
fun FindingDetailContent(
    model: FindingsDetailUiModel
) {
    val headerUiModel = model.header.copy(leftIcon = null)
    HeaderSection(headerUiModel = headerUiModel)

    Text(
        modifier = Modifier.padding(start = 20.dp, top = 20.dp),
        text = model.title.orEmpty(),
        style = TextStyle.HEADLINE_2.toTextStyle()
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        model.details.forEach { detail ->
            FindingContent(model = detail)
        }
    }
}

@Composable
private fun FindingContent(
    model: FindingDetailUiModel
) {
    val imagesBitmap = model.images.map { image ->
        image.decodeAsBase64Bitmap()
    }

    val pageCount = imagesBitmap.size

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clip(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
            .padding(25.dp)
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            pageCount
        }

        HorizontalPager(state = pagerState) { index ->
            Image(
                bitmap = imagesBitmap[index].asImageBitmap(),
                contentDescription = model.description.text,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }

        PagerIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            pageCount = pageCount,
            currentPage = pagerState.currentPage
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = model.description.text,
            style = model.description.textStyle.toTextStyle()
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = model.reporter.text,
            style = model.reporter.textStyle.toTextStyle()
        )
    }
}
