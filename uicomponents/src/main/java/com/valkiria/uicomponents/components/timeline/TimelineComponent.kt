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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

private const val FOUR = 4
private const val TWO = 2

@Composable
fun TimelineComponent(
    uiModel: TimelineUiModel
) {
    Column(
        modifier = uiModel.modifier
    ) {
        uiModel.items.forEachIndexed { index, timelineItem ->
            val position = when (index) {
                0 -> TimelinePosition.FIRST
                uiModel.items.lastIndex -> TimelinePosition.LAST
                else -> TimelinePosition.MIDDLE
            }

            TimelineNode(
                position = position,
                timelineItem = timelineItem
            ) {
                DetailInfoView(
                    modifier = it,
                    timelineItem = timelineItem
                )
            }
        }
    }
}

@Composable
private fun TimelineNode(
    position: TimelinePosition,
    timelineItem: TimelineItemUiModel,
    contentStartOffset: Dp = 48.dp,
    spacerBetweenNodes: Dp = 32.dp,
    content: @Composable BoxScope.(modifier: Modifier) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        timelineItem.icon.orEmpty(), DefType.DRAWABLE
    )

    val iconPainter = iconResourceId?.let { painterResource(id = it) }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .drawBehind {
                val circleRadiusInPx = 8.dp.toPx()
                if (position != TimelinePosition.LAST && iconResourceId == null) {
                    drawCircle(
                        color = Color(parseColor(timelineItem.color)),
                        radius = circleRadiusInPx,
                        center = Offset(circleRadiusInPx, circleRadiusInPx)
                    )
                }

                iconPainter?.let { painter ->
                    this.withTransform(
                        transformBlock = {
                            translate(
                                left = circleRadiusInPx - painter.intrinsicSize.width / 2f,
                                top = circleRadiusInPx - painter.intrinsicSize.height / 2f
                            )
                        },
                        drawBlock = {
                            this.drawIntoCanvas {
                                with(painter) {
                                    draw(
                                        intrinsicSize,
                                        colorFilter = ColorFilter.tint(
                                            Color(parseColor(timelineItem.color))
                                        )
                                    )
                                }
                            }
                        }
                    )
                }

                if (position != TimelinePosition.LAST) {
                    drawLine(
                        color = Color(parseColor(timelineItem.color)),
                        start = Offset(circleRadiusInPx, circleRadiusInPx * FOUR),
                        end = Offset(circleRadiusInPx, size.height - (circleRadiusInPx * TWO)),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
    ) {
        content(
            Modifier
                .padding(
                    start = contentStartOffset,
                    bottom = if (position == TimelinePosition.LAST) 0.dp else spacerBetweenNodes
                )
        )
    }
}

@Composable
private fun DetailInfoView(
    modifier: Modifier,
    timelineItem: TimelineItemUiModel
) {
    Column(
        modifier = modifier
    ) {
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
                    icon = "ic_alert"
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
                    icon = "ic_ambulance"
                )
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier
        )
    )
}
