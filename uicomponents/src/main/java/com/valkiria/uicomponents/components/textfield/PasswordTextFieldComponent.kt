package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.extensions.toFailedValidation
import com.valkiria.uicomponents.model.mocks.getLoginPasswordTextFieldUiModel
import com.valkiria.uicomponents.model.mocks.getNoIconPasswordTextFieldUiModel
import com.valkiria.uicomponents.model.props.toTextStyle
import com.valkiria.uicomponents.model.ui.textfield.PasswordTextFieldUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun PasswordTextFieldComponent(
    uiModel: PasswordTextFieldUiModel,
    isTablet: Boolean = false,
    validateFields: Boolean = false,
    onAction: (updatedValue: String, fieldValidated: Boolean) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon.orEmpty(), DefType.DRAWABLE
    )

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconResourceId?.let {
            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .size(42.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        var text by remember { mutableStateOf(TextFieldValue("")) }
        var showPassword by remember { mutableStateOf(value = false) }

        OutlinedTextField(
            value = text,
            onValueChange = { updatedValue ->
                text = updatedValue
                onAction(
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
            trailingIcon = {
                if (showPassword) {
                    IconButton(
                        onClick = { showPassword = false }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.ic_visibility_filled
                            ),
                            contentDescription = null
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.ic_visibility_off_filled
                            ),
                            contentDescription = null
                        )
                    }
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
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = uiModel.keyboardOptions,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldComponentPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray)
    ) {
        PasswordTextFieldComponent(
            uiModel = getLoginPasswordTextFieldUiModel()
        ) { updatedValue, fieldValidated ->
            Timber.d("Handle $updatedValue with $fieldValidated")
        }

        PasswordTextFieldComponent(
            uiModel = getNoIconPasswordTextFieldUiModel()
        ) { updatedValue, fieldValidated ->
            Timber.d("Handle $updatedValue with $fieldValidated")
        }
    }
}
