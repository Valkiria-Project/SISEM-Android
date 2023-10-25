package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.chip.FilterChipView
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.mocks.getPreOperationalFiltersUiModel
import timber.log.Timber

@Composable
fun FiltersComponent(
    uiModel: FiltersUiModel,
    onAction: (text: String, isSelection: Boolean) -> Unit
) {
    val selected = rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        uiModel.options.forEach { chipText ->
            FilterChipView(
                id = "",
                text = chipText,
                isSelected = (chipText == selected.value),
                textStyle = TextStyle.BUTTON_1,
                modifier = Modifier.padding(horizontal = 8.dp),
                onAction = { _, text, isSelection ->
                    selected.value = text
                    onAction(text, isSelection)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersComponentPreview() {
    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        FiltersComponent(
            uiModel = getPreOperationalFiltersUiModel()
        ) { selected, isSelection ->
            Timber.d("Selected $selected and is $isSelection")
        }
    }
}
