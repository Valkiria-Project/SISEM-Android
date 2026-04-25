package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.model.ui.textfield.PasswordTextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import com.valkiria.uicomponents.model.props.TextStyle

fun getLoginPasswordTextFieldUiModel(): PasswordTextFieldUiModel {
    return PasswordTextFieldUiModel(
        icon = "ic_lock",
        placeholder = "Contraseña",
        label = "Ingresar contraseña",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        textStyle = TextStyle.HEADLINE_5,
        validations = listOf(
            ValidationUiModel(
                regex = "^\\d{3}$",
                message = "La contraseña debe al menos tener 3 caracteres"
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 16.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}

fun getNoIconPasswordTextFieldUiModel(): PasswordTextFieldUiModel {
    return PasswordTextFieldUiModel(
        placeholder = "Contraseña",
        label = "Ingresar contraseña",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        textStyle = TextStyle.HEADLINE_5,
        validations = listOf(
            ValidationUiModel(
                regex = "^\\d{3}$",
                message = "La contraseña debe al menos tener 3 caracteres"
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 16.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}
