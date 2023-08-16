package com.valkiria.uicomponents.components.filters

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.FilterChipView
import com.valkiria.uicomponents.props.TextStyle
import timber.log.Timber

@Composable
fun FiltersComponent(
    uiModel: FiltersUiModel,
    onAction: (selected: String?, isSelection: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center
    ) {
        uiModel.options.forEach { chipText ->
            FilterChipView(
                text = chipText,
                textStyle = TextStyle.BUTTON_1,
                modifier = Modifier.padding(end = 18.dp),
                onAction = { selected, isSelection ->
                    onAction(selected, isSelection)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersComponentPreview() {
    val filtersUiModel = FiltersUiModel( // FIXME: Create Mock Model
        options = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
        textStyle = TextStyle.BUTTON_1
    )

    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        FiltersComponent(
            uiModel = filtersUiModel
        ) { selected, isSelection ->
            Timber.d("Selected $selected and is $isSelection")
        }
    }
}
