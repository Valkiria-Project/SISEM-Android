package com.valkiria.uicomponents.components.filterchips

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme

@Composable
fun FilterChipsComponent(
    uiModel: FilterChipsUiModel
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        uiModel.chips.map { chipText ->
            var selected by remember { mutableStateOf(false) }

            FilterChip(
                selected = selected,
                onClick = { selected = !selected },
                label = { Text(chipText) },
                modifier = Modifier.padding(end = 18.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipComponentPreview() {
    UiComponentsTheme {
        val filterChipsUiModel = FilterChipsUiModel( // FIXME: Create Mock Model
            chips = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
            textStyle = TextStyle.BUTTON_1
        )

        Column(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(16.dp)
        ) {
            FilterChipsComponent(
                uiModel = filterChipsUiModel
            )
        }
    }
}
