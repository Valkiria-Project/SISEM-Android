package com.valkiria.uicomponents.components.segmentedswitch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.valkiria.uicomponents.model.mocks.getDeviceAuthSegmentedSwitchUiModel
import com.valkiria.uicomponents.model.props.toTextStyle
import com.valkiria.uicomponents.model.ui.segmentedswitch.SegmentedSwitchUiModel

@Suppress("LongMethod", "MagicNumber", "UnusedPrivateMember")
@Composable
fun SegmentedSwitchComponent(
    uiModel: SegmentedSwitchUiModel,
    onAction: (id: String, status: Boolean) -> Unit
) {
    val items = uiModel.options.map { it.text }
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiModel.text,
            style = uiModel.textStyle.toTextStyle(),
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .padding(end = 8.dp),
            color = if (selectedIndex == 0) {
                Color.White
            } else {
                MaterialTheme.colorScheme.error
            }
        )

        Row(
            modifier = Modifier.wrapContentSize(),
        ) {
            items.forEachIndexed { index, item ->
                OutlinedButton(
                    modifier = when (index) {
                        0 -> {
                            Modifier
                                .wrapContentSize()
                                .offset(0.dp, 0.dp)
                                .zIndex(if (selectedIndex == index) 1f else 0f)
                        }

                        else -> {
                            Modifier
                                .wrapContentSize()
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex == index) 1f else 0f)
                        }
                    },
                    onClick = {
                        selectedIndex = index
                        onAction(uiModel.identifier, index == 0)
                    },
                    shape = when (index) {
                        /**
                         * left outer button
                         */
                        0 -> RoundedCornerShape(
                            topStartPercent = 50,
                            bottomStartPercent = 50,
                        )
                        /**
                         * right outer button
                         */
                        items.size - 1 -> RoundedCornerShape(
                            topEndPercent = 50,
                            bottomEndPercent = 50
                        )
                        /**
                         * middle button
                         */
                        else -> RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 0,
                            bottomStartPercent = 0,
                            bottomEndPercent = 0
                        )
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White
                    ),
                    colors = if (selectedIndex == index) {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedIndex == 0) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors(containerColor = Color.DarkGray)
                    },
                ) {
                    Text(
                        text = item,
                        color = if (selectedIndex == index) {
                            Color.Black
                        } else {
                            Color.White
                        },
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedSwitchComponentPreview() {
    SegmentedSwitchComponent(
        uiModel = getDeviceAuthSegmentedSwitchUiModel(),
        onAction = { _, _ -> }
    )
}
