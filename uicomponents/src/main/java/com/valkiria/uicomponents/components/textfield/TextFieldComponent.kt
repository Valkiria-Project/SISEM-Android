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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.props.toKeyBoardOption

@Composable
fun TextFieldComponent(
    model: TextFieldUiModel,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        model.icon?.let {
            Image(
                painter = painterResource(id = R.drawable.ic_usuario),
                contentDescription = "Icono de usuario",
                contentScale = ContentScale.FillWidth
            )
        }

        TextFieldUi(model = model)
    }

}

@Composable
private fun TextFieldUi(
    model: TextFieldUiModel,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        label = { Text(text = "Your Label") },
        placeholder = { Text(text = model.hint.orEmpty()) },
        keyboardOptions = model.keyboardType.toKeyBoardOption(),
    )
}
