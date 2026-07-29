package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.TextStyle

fun getLoginBlockedErrorUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_blocked",
        title = "Bloqueo",
        description = "El límite de intentos alcanzados ha sido superado, la cuenta será bloqueada."
    )
}

fun getLoginIncorrectErrorUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_alert",
        title = "Incorrecto",
        description = """Los datos de  usuario y/o contraseña son incorrectos. 
            |Por favor verifíquelos.
            """.trimMargin()
    )
}

fun getLoginPasswordErrorUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_alert",
        title = "Contraseña",
        description = "Su contraseña esta próxima a expirar, efectuar cambio la antes posible."
    )
}

fun getLoginDuplicatedErrorUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_duplicated",
        title = "Duplicidad",
        description = """El usuario ya se encuentra autenticado en 5421244, no es permitida
            | la conexión simultánea.
            """.trimMargin()
    )
}

fun getLoginDuplicatedWithActionsUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_duplicated",
        title = "Duplicidad",
        description = "Ya tiene una sesión activa en otro dispositivo o navegador. " +
            "¿Desea cerrarla e iniciar sesión aquí?",
        leftButton = bannerActionButton(
            identifier = "LOGIN_CLOSE_SESSION_CANCEL",
            label = "NO",
            style = ButtonStyle.TRANSPARENT,
            overrideColor = Color.White
        ),
        rightButton = bannerActionButton(
            identifier = "LOGIN_CLOSE_SESSION_CONFIRM",
            label = "SÍ",
            style = ButtonStyle.LOUD
        )
    )
}

private fun bannerActionButton(
    identifier: String,
    label: String,
    style: ButtonStyle,
    overrideColor: Color? = null
) = ButtonUiModel(
    identifier = identifier,
    label = label,
    style = style,
    overrideColor = overrideColor,
    textStyle = TextStyle.HEADLINE_5,
    onClick = OnClick.DISMISS,
    size = ButtonSize.DEFAULT,
    arrangement = Arrangement.Start,
    modifier = Modifier.padding(top = 20.dp)
)

fun getLoginUnassignedErrorUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_ambulance",
        title = "No asignado",
        description = """"El dispositivo no se encuentra asociado al vehículo, debe comunicarse
            | con el Líder APH de la subred.
            """.trimMargin()
    )
}
