package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.chip.OptionChipView
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.model.mocks.getPreOperationalChipOptionsUiModel
import com.valkiria.uicomponents.model.props.TextStyle
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun ChipOptionsComponent(
    uiModel: ChipOptionsUiModel,
    onAction: (id: String, text: String, isSelection: Boolean) -> Unit
) {
    Column(
        modifier = uiModel.modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        uiModel.title?.textStyle?.let {
            LabelComponent(
                uiModel = LabelUiModel(
                    identifier = uiModel.identifier,
                    text = uiModel.title.text,
                    textStyle = uiModel.title.textStyle
                )
            )
        }

        FlowRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            uiModel.items.forEach { chipOption ->
                OptionChipView(
                    id = chipOption.id,
                    text = chipOption.name,
                    textStyle = TextStyle.BUTTON_1,
                    onAction = { id, text, isSelection ->
                        onAction(id, text, isSelection)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipOptionsComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        ChipOptionsComponent(
            uiModel = getPreOperationalChipOptionsUiModel()
        ) { _, selected, isSelection ->
            Timber.d("Selected $selected and is $isSelection")
        }
    }
}
