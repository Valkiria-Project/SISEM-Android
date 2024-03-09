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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun OptionChipView(
    text: String,
    isSelected: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onAction: (isSelection: Boolean) -> Unit
) {
    var selected by remember { mutableStateOf(isSelected) }

    FilterChip(
        selected = selected,
        onClick = {
            selected = !selected
            onAction(selected)
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
        ),
        border = if (isError) {
            FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = isSelected,
                borderColor = MaterialTheme.colorScheme.error,
            )
        } else {
            FilterChipDefaults.filterChipBorder(
                enabled = true,
                selected = isSelected,
            )
        }
    )
}
