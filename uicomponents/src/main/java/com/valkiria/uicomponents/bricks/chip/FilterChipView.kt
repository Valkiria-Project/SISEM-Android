package com.valkiria.uicomponents.bricks.chip

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle

@Composable
fun FilterChipView(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    onAction: (selected: String?, isSelection: Boolean) -> Unit
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        selected = selected,
        onClick = {
            selected = !selected
            onAction(text, selected)
        },
        label = {
            Text(
                text = text,
                style = textStyle.toTextStyle(),
                color = if (selected) {
                    Color.Black
                } else {
                    Color.White
                }
            )
        },
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(20.dp)
    )
}