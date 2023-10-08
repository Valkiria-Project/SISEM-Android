package com.valkiria.uicomponents.bricks.chip

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
fun SuggestionChipView(
    text: String,
    textStyle: TextStyle,
    onAction: (Boolean) -> Unit = {}
) {
    var selected by remember { mutableStateOf(false) }

    SuggestionChip(
        onClick = {
            selected = !selected
            onAction(selected)
        },
        modifier = Modifier.wrapContentSize(),
        label = {
            Text(
                text = text,
                style = textStyle.toTextStyle(),
                color = Color.White
            )
        },
        shape = RoundedCornerShape(20.dp),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderColor = MaterialTheme.colorScheme.primary
        )
    )
}
