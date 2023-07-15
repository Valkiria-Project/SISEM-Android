package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import com.valkiria.uicomponents.R

@Composable
fun TextFieldComponent(
    uiModel: TextFieldUiModel
) {
    Row(modifier = uiModel.modifier.fillMaxWidth()) {
        uiModel.icon?.let {
            Image(
                painter = painterResource(id = R.drawable.ic_usuario),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
        }

        var text by remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text(text = "Your Label") },
            placeholder = { Text(text = uiModel.hint.orEmpty()) },
            keyboardOptions = uiModel.keyboardOptions
        )
    }
}
