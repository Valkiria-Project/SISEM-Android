package com.valkiria.uicomponents.bricks.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.extensions.toFailedValidation
import com.valkiria.uicomponents.mocks.getLoginUserTextFieldUiModel
import com.valkiria.uicomponents.props.toTextStyle

@Composable
fun FilledTextFieldView(
    uiModel: TextFieldUiModel,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit,
    validateFields: Boolean
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    TextField(
        value = text,
        onValueChange = { updatedValue ->
            text = updatedValue
            onAction(
                uiModel.identifier,
                updatedValue.text,
                text.toFailedValidation(uiModel.validations, validateFields) == null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        textStyle = uiModel.textStyle.toTextStyle(),
        label = {
            uiModel.label?.let { label ->
                Text(text = label)
            } ?: uiModel.placeholder?.let { label ->
                Text(text = label)
            }
        },
        supportingText = {
            if (validateFields) {
                Text(
                    text = text.toFailedValidation(uiModel.validations)?.message.orEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = text.toFailedValidation(uiModel.validations, validateFields) != null,
        keyboardOptions = uiModel.keyboardOptions,
        singleLine = uiModel.singleLine,
        minLines = uiModel.minLines
    )
}

@Preview(showBackground = true)
@Composable
fun FilledTextFieldViewPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray)
    ) {
        FilledTextFieldView(
            uiModel = getLoginUserTextFieldUiModel(),
            onAction = { _, _, _ -> },
            validateFields = true
        )
    }
}
