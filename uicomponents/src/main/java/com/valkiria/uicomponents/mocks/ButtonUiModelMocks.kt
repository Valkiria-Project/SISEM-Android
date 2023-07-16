package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.TextStyle

fun getLoginForgotButtonUiModel(): ButtonUiModel {
    /*
    {
        "label": "¿Olvidaste la contraseña?",
        "style": "TRANSPARENT",
        "text_style": "BUTTON_1",
        "on_click": "FORGOT_PASSWORD",
        "size": "DEFAULT",
        "identifier": "LOGIN_FORGOT_PASSWORD_BUTTON",
        "margins": {
            "top": 12,
            "left": 52,
            "right": 0,
            "bottom": 0
        },
        "type": "BUTTON"
    }
    */
    return ButtonUiModel(
        label = "¿Olvidaste la contraseña?",
        style = ButtonStyle.TRANSPARENT,
        textStyle = TextStyle.BUTTON_1,
        onClick = OnClick.FORGOT_PASSWORD,
        size = ButtonSize.DEFAULT,
        modifier = Modifier.padding(
            start = 52.dp,
            top = 12.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}
