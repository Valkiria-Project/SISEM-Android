package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.valkiria.uicomponents.components.passwordtextfield.PasswordTextFieldUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getLoginPasswordTextFieldUiModel(): PasswordTextFieldUiModel {
    /*
    {
    "identifier": "LOGIN_PASSWORD",
    "icon": "ic_lock",
    "placeholder": "Contraseña*",
    "keyboard_type": "PASSWORD",
    "text_style": "HEADLINE_5",
    "validations": [
        {
            "regex": "^\\d{3}$",
            "message": "La contraseña debe al menos tener 3 caracteres"
        }
    ],
    "type": "PASSWORD_TEXT_FIELD",
    "margins": {
        "top": 16,
        "left": 20,
        "right": 20,
        "bottom": 0
    }
    }
    */
    return PasswordTextFieldUiModel(
        icon = "ic_lock",
        placeholder = "Usuario",
        label = "Ingresar usuario",
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
        ),
        textStyle = TextStyle.HEADLINE_5,
        validations = listOf(
            ValidationUiModel(
                regex = "^(?!\\s*$).+",
                message = "El campo no debe estar vacío"
            )
        ),
        modifier = Modifier
    )
}
