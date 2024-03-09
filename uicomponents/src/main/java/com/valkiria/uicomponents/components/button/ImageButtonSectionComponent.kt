package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.extensions.toFailedValidation

@Composable
fun ImageButtonSectionComponent(
    uiModel: ImageButtonSectionUiModel,
    validateFields: Boolean = false,
    onAction: (id: String, itemId: String) -> Unit
) {
    val isError = remember(uiModel.selected, validateFields) {
        uiModel.selected.toFailedValidation(validateFields)
    }

    Column(
        modifier = uiModel.modifier.fillMaxWidth(),
    ) {
        uiModel.options.forEach { option ->
            ImageButtonOptionView(
                uiModel = option,
                isError = isError,
                onAction = { id ->
                    onAction(uiModel.identifier, id)
                }
            )
        }
    }
}

@Composable
private fun ImageButtonOptionView(
    uiModel: ImageButtonOptionUiModel,
    isError: Boolean = false,
    onAction: (id: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        uiModel.options.forEach { option ->
            ImageButtonView(uiModel = option, isError = isError) { id ->
                onAction(id)
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = uiModel.title.text,
            style = uiModel.title.textStyle.toTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageButtonSectionComponentPreview() {
    val uiModel = ImageButtonSectionUiModel(
        identifier = "12345",
        options = listOf(
            ImageButtonOptionUiModel(
                identifier = "12345",
                title = TextUiModel(
                    text = "Normal",
                    textStyle = TextStyle.HEADLINE_2
                ),
                options = listOf(
                    ImageButtonUiModel(
                        identifier = "1",
                        title = null,
                        image = "ic_normal_eyes",
                        selected = false,
                        arrangement = Arrangement.Center,
                        modifier = Modifier
                    )
                ),
                modifier = Modifier
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
    ImageButtonSectionComponent(uiModel = uiModel) { _, _ -> }
}
