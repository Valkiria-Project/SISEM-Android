package com.valkiria.uicomponents.components.segmentedswitch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle

@Composable
fun SegmentedSwitchComponent(
    uiModel: SegmentedSwitchUiModel,
    isTablet: Boolean
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiModel.text,
            style = uiModel.textStyle.toTextStyle(),
            modifier = Modifier.fillMaxWidth(0.4f)
        )

        val items = uiModel.options.map { it.text }
        val selectedIndex = remember { mutableStateOf(0) }
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
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }

                        else -> {
                            Modifier
                                .wrapContentSize()
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (selectedIndex.value == index) 1f else 0f)
                        }
                    },
                    onClick = {
                        selectedIndex.value = index
                        /*onItemSelection(selectedIndex.value)*/
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
                    colors = if (selectedIndex.value == index) {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedIndex.value == 0) {
                                Color(0xFF3cf2dd)
                            } else {
                                Color(0xFFf55757)
                            }
                        )
                    } else {
                        ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                    },
                ) {
                    Text(
                        text = item,
                        color = if (selectedIndex.value == index) {
                            Color.Black
                        } else {
                            Color.White
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SegmentedSwitchComponentPreview() {
    SegmentedSwitchComponent(
        uiModel = SegmentedSwitchUiModel(
            text = "Prueba algo de texto",
            textStyle = TextStyle.HEADLINE_5,
            options = listOf(
                OptionUiModel(
                    text = "No",
                    textStyle = TextStyle.BUTTON_2,
                    color = "blueColor"
                ),
                OptionUiModel(
                    text = "Si",
                    textStyle = TextStyle.BUTTON_2,
                    color = "red"
                )
            ),
            modifier = Modifier
        ),
        isTablet = false
    )
}
