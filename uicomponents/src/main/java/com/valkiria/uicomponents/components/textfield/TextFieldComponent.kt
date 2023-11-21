package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.textfield.DatePickerTextFieldView
import com.valkiria.uicomponents.bricks.textfield.FilledTextFieldView
import com.valkiria.uicomponents.bricks.textfield.FixedDateTextFieldView
import com.valkiria.uicomponents.bricks.textfield.OutlinedTextFieldView
import com.valkiria.uicomponents.mocks.getLoginUserTextFieldUiModel
import com.valkiria.uicomponents.mocks.getPreOpDriverVehicleKMTextFieldUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Composable
fun TextFieldComponent(
    uiModel: TextFieldUiModel,
    validateFields: Boolean = false,
    onAction: (inputUiModel: InputUiModel) -> Unit
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
                imageVector = ImageVector.vectorResource(id = iconResourceId),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .size(42.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        when (uiModel.style) {
            TextFieldStyle.DATE_PICKER -> DatePickerTextFieldView(
                uiModel = uiModel,
                validateFields = validateFields || uiModel.realTimeValidation
            ) { id, updatedValue, fieldValidated, required ->
                onAction(InputUiModel(id, updatedValue, fieldValidated, required))
            }

            TextFieldStyle.FILLED -> FilledTextFieldView(
                uiModel = uiModel,
                validateFields = validateFields || uiModel.realTimeValidation
            ) { id, updatedValue, fieldValidated, required ->
                onAction(InputUiModel(id, updatedValue, fieldValidated, required))
            }

            TextFieldStyle.FIXED_DATE -> FixedDateTextFieldView(
                uiModel = uiModel,
                validateFields = validateFields || uiModel.realTimeValidation
            ) { id, updatedValue, fieldValidated, required ->
                onAction(InputUiModel(id, updatedValue, fieldValidated, required))
            }

            TextFieldStyle.OUTLINED -> OutlinedTextFieldView(
                uiModel = uiModel,
                validateFields = validateFields || uiModel.realTimeValidation
            ) { id, updatedValue, fieldValidated, required ->
                onAction(InputUiModel(id, updatedValue, fieldValidated, required))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldComponentPreview() {
    Column(
        modifier = Modifier.background(Color.DarkGray)
    ) {
        TextFieldComponent(
            uiModel = getLoginUserTextFieldUiModel()
        ) { inputUiModel ->
            Timber.d("Handle ${inputUiModel.updatedValue} with ${inputUiModel.fieldValidated}")
        }
        TextFieldComponent(
            uiModel = getPreOpDriverVehicleKMTextFieldUiModel()
        ) { inputUiModel ->
            Timber.d("Handle ${inputUiModel.updatedValue} with ${inputUiModel.fieldValidated}")
        }
    }
}
