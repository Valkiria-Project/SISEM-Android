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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.extensions.toFailedValidation
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel

@Composable
fun DigitsTextFieldView(
    identifier: String,
    style: TextStyle,
    validateFields: Boolean = false,
    onAction: (id: String, updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val validations = listOf(
        ValidationUiModel(
            regex = "^(?!\\s*$).+",
            message = "El campo no debe estar vacío"
        )
    )

    OutlinedTextField(
        value = text,
        onValueChange = { updatedValue ->
            text = updatedValue
            onAction(
                identifier,
                updatedValue.text,
                text.toFailedValidation(validations, validateFields) == null
            )
        },
        modifier = Modifier.size(56.dp),
        textStyle = style,
        supportingText = {
            if (validateFields) {
                Text(
                    text = "El campo no debe estar vacío",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = text.toFailedValidation(validations, validateFields) != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}
