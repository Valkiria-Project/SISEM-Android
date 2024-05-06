package com.valkiria.uicomponents.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.chip.FilterChipView
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.extensions.toFailedValidation

private const val NO = "NO"

@Composable
fun ChipSelectionComponent(
    uiModel: ChipSelectionUiModel,
    validateFields: Boolean = false,
    onAction: (
        id: String,
        selectionItem: ChipSelectionItemUiModel,
        isSelection: Boolean,
        viewsVisibility: Map<String, Boolean>,
        viewsInvisibility: Map<String, Boolean>
    ) -> Unit
) {
    val selected = remember { mutableStateOf(uiModel.selected) }

    val isError = remember(selected.value, validateFields) {
        selected.value.toFailedValidation(validateFields)
    }

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
            uiModel.items.forEach { chipSelection ->
                FilterChipView(
                    id = chipSelection.id,
                    text = chipSelection.name,
                    isSelected = (
                        chipSelection.name == selected.value ||
                            chipSelection.id == selected.value
                        ),
                    textStyle = TextStyle.BUTTON_1,
                    isError = isError,
                    onAction = { id, text, isSelection ->
                        selected.value = text

                        val viewsVisibility = mutableMapOf<String, Boolean>()
                        uiModel.selectionVisibility?.forEach {
                            val visibility =
                                it.value.find { value -> value.equals(id, ignoreCase = true) }
                            viewsVisibility[it.key] = visibility != null
                        }

                        val viewsInvisibility = mutableMapOf<String, Boolean>()
                        if (text.equals(NO, true)) {
                            uiModel.deselectionVisibility?.forEach {
                                val invisibility =
                                    it.value.find { value -> value.equals(id, ignoreCase = true) }
                                viewsInvisibility[it.key] = invisibility == null
                            }
                        }

                        onAction(
                            uiModel.identifier,
                            chipSelection,
                            isSelection,
                            viewsVisibility,
                            viewsInvisibility
                        )
                    }
                )
            }
        }
    }
}
