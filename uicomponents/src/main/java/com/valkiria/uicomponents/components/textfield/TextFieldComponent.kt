package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.FilledTextFieldView
import com.valkiria.uicomponents.bricks.OutlinedTextFieldView
import com.valkiria.uicomponents.mocks.getLoginUserTextFieldUiModel
import com.valkiria.uicomponents.mocks.getPreOpDriverVehicleKMTextFieldUiModel
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.props.TextFieldStyle
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
            uiModel.modifier.width(TabletWidth)
        } else {
            uiModel.modifier.fillMaxWidth()
        },
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

        if (uiModel.style == TextFieldStyle.OUTLINED) {
            OutlinedTextFieldView(
                uiModel = uiModel,
                validateFields = validateFields,
                onAction = onAction
            )
        } else {
            FilledTextFieldView(
                uiModel = uiModel,
                validateFields = validateFields,
                onAction = onAction
            )
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
        ) { updatedValue, fieldValidated ->
            Timber.d("Handle $updatedValue with $fieldValidated")
        }
        TextFieldComponent(
            uiModel = getPreOpDriverVehicleKMTextFieldUiModel()
        ) { updatedValue, fieldValidated ->
            Timber.d("Handle $updatedValue with $fieldValidated")
        }
    }
}
