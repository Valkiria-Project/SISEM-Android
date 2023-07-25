package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.mocks.getLoginUserTextFieldUiModel
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Composable
fun TextFieldComponent(
    uiModel: TextFieldUiModel,
    isTablet: Boolean = false,
    validateFields: Boolean = false,
    onAction: (updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon.orEmpty(), DefType.DRAWABLE
    )

    Row(
        modifier = if (isTablet) {
            uiModel.modifier.width(300.dp)
        } else {
            uiModel.modifier.fillMaxWidth()
        },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconResourceId?.let {
            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(42.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        var text by remember { mutableStateOf(TextFieldValue("")) }

        OutlinedTextField(
            value = text,
            onValueChange = { updatedValue ->
                text = updatedValue
                onAction(
                    updatedValue.text,
                    text.toFailedValidation(uiModel.validations, validateFields) == null
                )
            },
            modifier = Modifier.imePadding(),
            textStyle = uiModel.textStyle.toTextStyle(),
            label = {
                uiModel.label?.let { label ->
                    Text(text = label)
                } ?: uiModel.placeholder?.let { label ->
                    Text(text = label)
                }
            },
            placeholder = {
                // BACKEND: Check this with the team
                /*
                uiModel.placeholder?.let { placeholder ->
                    Text(text = placeholder)
                }
                */
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
            singleLine = true
        )
    }
}

private fun TextFieldValue.toFailedValidation(
    validations: List<ValidationUiModel>,
    validateFields: Boolean = true
): ValidationUiModel? {
    if (validateFields.not()) {
        return null
    }

    val invalidRegex = validations.find {
        text.matches(it.regex.toRegex()).not()
    }

    return invalidRegex
}

@Preview(showBackground = true)
@Composable
fun TextFieldComponentPreview() {
    UiComponentsTheme {
        Column(
            modifier = Modifier.background(Color.DarkGray)
        ) {
            TextFieldComponent(
                uiModel = getLoginUserTextFieldUiModel()
            ) { updatedValue, fieldValidated ->
                Timber.d("Handle $updatedValue with $fieldValidated")
            }
        }
    }
}
