package com.valkiria.uicomponents.bricks.chip

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun OptionChipView(
    id: String,
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    onAction: (id: String, isSelection: Boolean) -> Unit
) {
    var selected by rememberSaveable { mutableStateOf(false) }

    FilterChip(
        selected = selected,
        onClick = {
            selected = !selected
            onAction(id, selected)
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
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}
