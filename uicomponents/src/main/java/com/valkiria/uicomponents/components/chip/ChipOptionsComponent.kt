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
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.mocks.getPreOperationalChipOptionsUiModel
import timber.log.Timber

@Composable
fun ChipOptionsComponent(
    uiModel: ChipOptionsUiModel,
    onAction: (id: String, option: ChipOptionUiModel) -> Unit
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
                    textStyle = uiModel.title.textStyle,
                    arrangement = Arrangement.Start
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
                val viewsVisibility = mutableMapOf<String, Boolean>()
                uiModel.selectionVisibility?.entries?.forEach { entry ->
                    entry.value.find { it == chipOption.id }?.let {
                        viewsVisibility[entry.key] = chipOption.selected
                    }
                }

                OptionChipView(
                    text = chipOption.name,
                    isSelected = chipOption.selected,
                    textStyle = TextStyle.BUTTON_1,
                    onAction = { isSelection ->
                        uiModel.selectionVisibility?.entries?.forEach { entry ->
                            entry.value.find { it == chipOption.id }?.let {
                                viewsVisibility[entry.key] = isSelection
                            }
                        }
                        val updatedChipOption = chipOption.copy(
                            selected = isSelection,
                            viewsVisibility = viewsVisibility
                        )

                        onAction(uiModel.identifier, updatedChipOption)
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
        ) { _, chipOptionUiModel ->
            Timber.d("Selected ${chipOptionUiModel.name} and is ${chipOptionUiModel.selected}")
        }
    }
}
