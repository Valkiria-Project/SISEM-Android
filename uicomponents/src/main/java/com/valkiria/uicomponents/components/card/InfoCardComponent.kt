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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.bricks.chip.SuggestionChipView
import com.valkiria.uicomponents.model.props.toTextStyle
import com.valkiria.uicomponents.model.ui.card.InfoCardUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipSectionUiModel
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

private const val MAX_FINDINGS = 3

@Suppress("LongMethod", "UnusedPrivateMember")
@Composable
fun InfoCardComponent(
    uiModel: InfoCardUiModel,
    onAction: () -> Unit,
    onNewsAction: (reportDetail: ReportsDetailUiModel) -> Unit,
    onFindingsAction: (chipSection: ChipSectionUiModel) -> Unit
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
                Row(verticalAlignment = Alignment.Bottom) {
                    iconResourceId?.let {
                        Icon(
                            painter = painterResource(id = iconResourceId),
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp, end = 8.dp)) {
                        Text(
                            text = uiModel.titleText,
                            style = uiModel.titleTextStyle.toTextStyle(),
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .background(
                                    color = Color(parseColor(uiModel.pillColor)),
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .padding(horizontal = 8.dp),
                            text = uiModel.pillText,
                            style = uiModel.pillTextStyle.toTextStyle(),
                            color = Color.Black
                        )
                    }

                    uiModel.reportsDetail?.let {
                        BadgedBoxView(
                            count = it.details.size,
                            reportDetail = it,
                            onAction = onNewsAction
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = uiModel.dateText?.split(" - ")?.firstOrNull().orEmpty(),
                        style = uiModel.dateTextStyle.toTextStyle(),
                        modifier = Modifier.padding(end = 12.dp),
                    )

                    Icon(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 8.dp),
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = uiModel.dateText?.split(" - ")?.lastOrNull().orEmpty(),
                        style = uiModel.dateTextStyle.toTextStyle(),
                    )
                }

                uiModel.chipSection?.let { it ->
                    Text(
                        text = it.title,
                        style = it.titleTextStyle.toTextStyle(),
                        modifier = Modifier.padding(top = 10.dp),
                    )

                    FlowRow(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        it.listText.forEachIndexed { index, text ->
                            if (index < MAX_FINDINGS) {
                                SuggestionChipView(text = text, textStyle = it.listTextStyle)
                            } else {
                                SuggestionChipView(
                                    text = "...",
                                    textStyle = it.listTextStyle,
                                ) { _ ->
                                    onFindingsAction(it)
                                }

                                return@forEachIndexed
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
    count: Int,
    reportDetail: ReportsDetailUiModel,
    onAction: (reportDetail: ReportsDetailUiModel) -> Unit
) {
    BadgedBox(
        modifier = Modifier
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
            painter = painterResource(id = R.drawable.ic_news),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
