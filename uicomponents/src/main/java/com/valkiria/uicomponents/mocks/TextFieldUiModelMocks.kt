package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getLoginTextFieldUiModel(): TextFieldUiModel {
    /*
    {
       "identifier": "LOGIN_EMAIL",
       "icon": "ic_user",
       "placeholder": "Usuario*",
       "keyboard_type": "TEXT",
       "text_style": "HEADLINE_5",
       "validations": [
           {
               "regex": "^(?!\\s*$).+",
               "message": "El campo no debe estar vacío"
           }
       ],
       "type": "TEXT_FIELD",
       "margins": {
           "top": 20,
           "left": 20,
           "right": 20,
           "bottom": 0
       }
    }
    */
    return TextFieldUiModel(
        icon = "ic_user",
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
