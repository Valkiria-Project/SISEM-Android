package com.valkiria.uicomponents.components.crewmembercard

import android.graphics.Color.parseColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@OptIn(ExperimentalLayoutApi::class)
@Suppress("LongMethod", "UnusedPrivateMember")
@Composable
fun CrewMemberCardComponent(
    uiModel: CrewMemberCardUiModel,
    isTablet: Boolean = false,
    onAction: () -> Unit,
    onNewsAction: () -> Unit
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
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
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
                        BadgedBoxView(count = it.details.size, onAction = onNewsAction)
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
                        text = uiModel.dateText.split(" - ").first(),
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
                        text = uiModel.dateText.split(" - ").last(),
                        style = uiModel.dateTextStyle.toTextStyle(),
                    )
                }

                uiModel.chipSection?.let {
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
                        it.listText.forEach { text ->
                            ChipView(text = text, textStyle = it.listTextStyle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgedBoxView(count: Int, onAction: () -> Unit) {
    BadgedBox(
        modifier = Modifier
            .clickable { onAction() }
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

@Composable
fun ChipView(text: String, textStyle: TextStyle) {
    SuggestionChip(
        onClick = { /*TODO*/ },
        modifier = Modifier.wrapContentSize(),
        label = {
            Text(
                text = text,
                style = textStyle.toTextStyle(),
                color = Color.White
            )
        },
        shape = RoundedCornerShape(25.dp),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderColor = MaterialTheme.colorScheme.primary
        )
    )
}
