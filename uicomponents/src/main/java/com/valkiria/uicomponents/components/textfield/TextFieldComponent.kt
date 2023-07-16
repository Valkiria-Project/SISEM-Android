package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.valkiria.uicomponents.mocks.getLoginTextFieldUiModel
import com.valkiria.uicomponents.theme.UiComponentsTheme
import com.valkiria.uicomponents.utlis.DefType.DRAWABLE
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun TextFieldComponent(
    uiModel: TextFieldUiModel
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon.orEmpty(), DRAWABLE
    )

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconResourceId?.let {
            Icon(
                painter = painterResource(id = iconResourceId),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        var text by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = {
                uiModel.label?.let { label ->
                    Text(text = label)
                } ?: uiModel.placeholder?.let { label ->
                    Text(text = label)
                }
            },
            placeholder = {
                /*
                uiModel.placeholder?.let { placeholder ->
                    Text(text = placeholder)
                }
                */
            },
            keyboardOptions = uiModel.keyboardOptions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldComponentPreview() {
    UiComponentsTheme {
        Column(
            modifier = Modifier.background(Color.DarkGray)
        ) {
            TextFieldComponent(uiModel = getLoginTextFieldUiModel())
        }
    }
}
