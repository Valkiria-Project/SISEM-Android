package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel

fun getLoginForgotButtonUiModel(): ButtonUiModel {
    return ButtonUiModel(
        identifier = "forgot_password",
        label = "¿Olvidaste la contraseña?",
        style = ButtonStyle.TRANSPARENT,
        textStyle = TextStyle.BUTTON_1,
        onClick = OnClick.FORGOT_PASSWORD,
        size = ButtonSize.DEFAULT,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 52.dp,
            top = 12.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}

fun getLoginButtonUiModel(): ButtonUiModel {
    return ButtonUiModel(
        identifier = "login",
        label = "INGRESAR",
        style = ButtonStyle.LOUD,
        textStyle = TextStyle.HEADLINE_5,
        onClick = OnClick.LOGIN,
        size = ButtonSize.FULL_WIDTH,
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 20.dp,
            top = 20.dp,
            end = 20.dp,
            bottom = 0.dp
        )
    )
}
