package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.chip.FilterChipView
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.ChipSelectionUiModel
import com.valkiria.uicomponents.model.ui.body.LabelUiModel

@Suppress("UnusedPrivateMember")
@Composable
fun ChipSelectionComponent(
    uiModel: ChipSelectionUiModel,
    isTablet: Boolean = false,
    onAction: (id: String, text: String, isSelection: Boolean) -> Unit
) {
    val selected = rememberSaveable { mutableStateOf("") }

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
                FilterChipView(
                    id = "",
                    text = chipOption.name,
                    isSelected = (chipOption.name == selected.value),
                    textStyle = TextStyle.BUTTON_1,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onAction = { _, text, isSelection ->
                        selected.value = text
                        onAction(uiModel.identifier, text, isSelection)
                    }
                )
            }
        }
    }
}
