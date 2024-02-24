package com.valkiria.uicomponents.bricks.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.extensions.toFailedValidation

@Suppress("LongParameterList")
@Composable
fun DigitsTextFieldView(
    identifier: String,
    value: String,
    style: TextStyle,
    validateFields: Boolean = false,
    validations: List<ValidationUiModel>,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(value))
    }

    val inputError = remember(text, validateFields) {
        text.toFailedValidation(validations, validateFields)
    }

    OutlinedTextField(
        value = text,
        onValueChange = { updatedValue ->
            text = updatedValue
            onAction(
                identifier,
                updatedValue.text,
                text.toFailedValidation(validations, true) == null
            )
        },
        modifier = Modifier.size(64.dp),
        textStyle = style,
        supportingText = {
            if (inputError != null) {
                Text(
                    text = inputError.message,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = inputError != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}
