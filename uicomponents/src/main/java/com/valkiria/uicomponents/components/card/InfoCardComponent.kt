package com.valkiria.uicomponents.components.card

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.banner.report.ReportDetailUiModel
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.bricks.chip.SuggestionChipView
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

private const val MAX_FINDINGS = 3

@Suppress("LongMethod", "UnusedPrivateMember")
@Composable
fun InfoCardComponent(
    uiModel: InfoCardUiModel,
    onAction: () -> Unit,
    onNewsAction: (reportDetail: ReportsDetailUiModel) -> Unit = {},
    onFindingsAction: (chipSection: ChipSectionUiModel) -> Unit = {}
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon, DefType.DRAWABLE
    )

    val brush = Brush.horizontalGradient(
        colors = listOf(
            Color.Black,
            MaterialTheme.colorScheme.background
        )
    )

    ElevatedCard(
        modifier = uiModel.modifier
            .fillMaxWidth()
            .shadow(
                elevation = 25.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black
            ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier
                .clickable { onAction() }
                .background(brush = brush)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (icon, text, badged) = createRefs()

                    iconResourceId?.let {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = iconResourceId),
                            contentDescription = "",
                            modifier = Modifier
                                .constrainAs(icon) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .size(40.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Column(
                        modifier = Modifier
                            .constrainAs(text) {
                                start.linkTo(icon.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(badged.start)
                                width = Dimension.preferredWrapContent
                            }
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = uiModel.title.text,
                            style = uiModel.title.textStyle.toTextStyle(),
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .background(
                                    color = Color(parseColor(uiModel.pill.color)),
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .padding(horizontal = 8.dp),
                            text = uiModel.pill.title.text,
                            style = uiModel.pill.title.textStyle.toTextStyle(),
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    uiModel.reportsDetail?.let {
                        BadgedBoxView(
                            modifier = Modifier
                                .constrainAs(badged) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(start = 12.dp),
                            count = it.details.size,
                            reportDetail = it,
                            onAction = onNewsAction
                        )
                    }
                }

                uiModel.date?.textStyle?.let {
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 8.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendar),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                        )

                        Text(
                            text = uiModel.date.text.split(" - ").firstOrNull().orEmpty(),
                            style = uiModel.date.textStyle.toTextStyle(),
                            modifier = Modifier.padding(end = 12.dp),
                        )

                        Icon(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 8.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_clock),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                        )

                        Text(
                            text = uiModel.date.text.split(" - ").lastOrNull().orEmpty(),
                            style = uiModel.date.textStyle.toTextStyle(),
                        )
                    }
                }

                uiModel.chipSection?.let { it ->
                    Text(
                        text = it.title.text,
                        style = it.title.textStyle.toTextStyle(),
                        modifier = Modifier.padding(top = 10.dp),
                    )

                    FlowRow(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        var count = 0

                        it.listText.texts.forEachIndexed { index, text ->
                            when {
                                uiModel.date?.textStyle == null ->
                                    SuggestionChipView(
                                        text = text,
                                        textStyle = it.listText.textStyle
                                    )

                                index < MAX_FINDINGS ->
                                    SuggestionChipView(
                                        text = text,
                                        textStyle = it.listText.textStyle
                                    )

                                count == 0 -> {
                                    SuggestionChipView(
                                        text = "...",
                                        textStyle = it.listText.textStyle,
                                    ) { _ ->
                                        onFindingsAction(it)
                                    }

                                    count++
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgedBoxView(
    modifier: Modifier = Modifier,
    count: Int,
    reportDetail: ReportsDetailUiModel,
    onAction: (reportDetail: ReportsDetailUiModel) -> Unit
) {
    BadgedBox(
        modifier = modifier
            .clickable { onAction(reportDetail) }
            .size(25.dp),
        badge = {
            Badge {
                Text(text = count.toString())
            }
        },
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_news),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true)
@Composable
fun InfoCardComponentPreview() {
    InfoCardComponent(
        uiModel = InfoCardUiModel(
            identifier = "identifier",
            icon = "ic_aux",
            title = TextUiModel(
                text = "Title",
                textStyle = TextStyle.HEADLINE_4
            ),
            pill = PillUiModel(
                title = TextUiModel(
                    text = "Anterior: RODOLFO BARRIOS GOMEZ",
                    textStyle = TextStyle.HEADLINE_7
                ),
                color = "#FF0000"
            ),
            date = TextUiModel(
                text = "10:30",
                textStyle = TextStyle.HEADLINE_4
            ),
            chipSection = ChipSectionUiModel(
                title = TextUiModel(
                    text = "ChipSection",
                    textStyle = TextStyle.HEADLINE_7
                ),
                listText = ListTextUiModel(
                    listOf("Prueba1", "Prueba2"),
                    textStyle = TextStyle.HEADLINE_7
                )
            ),
            reportsDetail = ReportsDetailUiModel(
                header = HeaderUiModel(
                    identifier = "identifier",
                    title = TextUiModel(
                        text = "Title",
                        textStyle = TextStyle.HEADLINE_4
                    ),
                    arrangement = Arrangement.Center,
                    modifier = Modifier
                ),
                details = listOf(
                    ReportDetailUiModel(
                        images = listOf("ic_aux"),
                        title = TextUiModel(
                            text = "Title",
                            textStyle = TextStyle.HEADLINE_4
                        ),
                        subtitle = TextUiModel(
                            text = "Subtitle",
                            textStyle = TextStyle.HEADLINE_7
                        ),
                        description = TextUiModel(
                            text = "Description",
                            textStyle = TextStyle.HEADLINE_7
                        ),
                        modifier = Modifier
                    )
                )
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier
        ),
        onAction = {},
        onNewsAction = {},
        onFindingsAction = {}
    )
}
