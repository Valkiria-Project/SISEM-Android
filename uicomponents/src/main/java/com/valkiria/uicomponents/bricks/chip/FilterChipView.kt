package com.valkiria.uicomponents.bricks.chip

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.toTextStyle

@Suppress("LongParameterList")
@Composable
fun FilterChipView(
    id: String,
    text: String,
    isSelected: Boolean,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onAction: (id: String, text: String, isSelection: Boolean) -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = {
            onAction(id, text, !isSelected)
        },
        label = {
            Text(
                text = text,
                style = textStyle.toTextStyle(),
                color = if (isSelected) {
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
