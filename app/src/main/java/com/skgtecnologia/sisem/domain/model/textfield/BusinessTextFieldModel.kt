package com.skgtecnologia.sisem.domain.model.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

fun sendEmailTextField(): TextFieldUiModel = TextFieldUiModel(
    identifier = "FROM_EMAIL_TEXT_FIELD",
    placeholder = "Para",
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    textStyle = TextStyle.HEADLINE_5,
    validations = listOf(
        ValidationUiModel(
            regex = "^(?!\\\\s*\$).+",
            message = "El campo no puede estar vacío"
        )
    ),
    charLimit = 300,
    arrangement = Arrangement.Center,
    modifier = Modifier.padding(
        start = 20.dp,
        top = 20.dp,
        end = 20.dp,
        bottom = 0.dp
    )
)
