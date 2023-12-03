package com.valkiria.uicomponents.components.timeline

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun TimelineComponent(
    uiModel: TimelineUiModel
) {
    Column(
        modifier = uiModel.modifier
    ) {
        uiModel.items.forEachIndexed { index, timelineItem ->
            TimelineNode(
                timelineItem = timelineItem,
                position = when (index) {
                    0 -> TimelinePosition.FIRST
                    uiModel.items.size -1 -> TimelinePosition.LAST
                    else -> TimelinePosition.MIDDLE
                }
            ) {
                DetailInfoView(
                    modifier = Modifier,
                    timelineItem = timelineItem
                )
            }
        }
    }
}

@Composable
private fun TimelineNode(
    timelineItem: TimelineItemUiModel,
    position: TimelinePosition,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        content(
            Modifier
                .padding(start = 16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color(parseColor(timelineItem.color)),
                        radius = 8.dp.toPx()
                    )
                    drawLine(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(parseColor(timelineItem.color)),
                                Color(parseColor(timelineItem.color)),
                            )
                        ),
                        start = Offset(8.dp.toPx(), 16.dp.toPx()),
                        end = Offset(8.dp.toPx(), size.height),
                        strokeWidth = 3.dp.toPx()
                    )
                }
        )
    }
}

@Composable
private fun DetailInfoView(
    modifier: Modifier,
    timelineItem: TimelineItemUiModel
) {
    Column {
        Text(
            text = timelineItem.title.text,
            style = timelineItem.title.textStyle.toTextStyle()
        )
        Text(
            text = timelineItem.description.text,
            style = timelineItem.description.textStyle.toTextStyle()
        )
    }
}

@Preview
@Composable
fun TimelineComponentPreview() {
    TimelineComponent(
        uiModel = TimelineUiModel(
            identifier = "12345",
            items = listOf(
                TimelineItemUiModel(
                    title = TextUiModel(
                        text = "Title 1",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    description = TextUiModel(
                        text = "Title 1",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    color = "#FF42A4",
                    icon = null
                ),
                TimelineItemUiModel(
                    title = TextUiModel(
                        text = "Title 1",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    description = TextUiModel(
                        text = "Title 1",
                        textStyle = TextStyle.HEADLINE_5
                    ),
                    color = "#FF42A4",
                    icon = null
                )
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier
        )
    )
}
