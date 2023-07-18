package com.valkiria.uicomponents.mocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel
import com.valkiria.uicomponents.props.TextStyle

@Composable
fun getLoginBlockedErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = painterResource(id = drawable.ic_blocked),
        title = "Bloqueo",
        titleTextStyle = TextStyle.HEADLINE_1,
        text = "El límite de intentos alcanzados ha sido superado, la cuenta será bloqueada.",
        textStyle = TextStyle.BODY_1
    )
}

@Composable
fun getLoginIncorrectErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = painterResource(id = drawable.ic_warning),
        title = "Incorrecto",
        titleTextStyle = TextStyle.HEADLINE_1,
        text = "Los datos de  usuario y/o contraseña son incorrectos. Por favor verifíquelos.",
        textStyle = TextStyle.BODY_1
    )
}

@Composable
fun getLoginPasswordErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = painterResource(id = drawable.ic_warning),
        title = "Contraseña",
        titleTextStyle = TextStyle.HEADLINE_1,
        text = "Su contraseña esta próxima a expirar, efectuar cambio la antes posible.",
        textStyle = TextStyle.BODY_1
    )
}

@Composable
fun getLoginDuplicatedErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = painterResource(id = drawable.ic_duplicated),
        title = "Duplicidad",
        titleTextStyle = TextStyle.HEADLINE_1,
        text = "El usuario ya se encuentra autenticado en 5421244, no es permitida la conexión simultánea.",
        textStyle = TextStyle.BODY_1
    )
}

@Composable
fun getLoginUnassignedErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = painterResource(id = drawable.ic_ambulance),
        title = "No asignado",
        titleTextStyle = TextStyle.HEADLINE_1,
        text = "El dispositivo no se encuentra asociado al vehículo, debe comunicarse con el Líder APH de la subred.",
        textStyle = TextStyle.BODY_1
    )
}
