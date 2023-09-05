package com.valkiria.uicomponents.model.mocks

import com.valkiria.uicomponents.components.errorbanner.ErrorUiModel

fun getLoginBlockedErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = "ic_blocked",
        title = "Bloqueo",
        description = "El límite de intentos alcanzados ha sido superado, la cuenta será bloqueada."
    )
}

fun getLoginIncorrectErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = "ic_alert",
        title = "Incorrecto",
        description = """Los datos de  usuario y/o contraseña son incorrectos. 
            |Por favor verifíquelos.""".trimMargin()
    )
}

fun getLoginPasswordErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = "ic_alert",
        title = "Contraseña",
        description = "Su contraseña esta próxima a expirar, efectuar cambio la antes posible."
    )
}

fun getLoginDuplicatedErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = "ic_duplicated",
        title = "Duplicidad",
        description = """El usuario ya se encuentra autenticado en 5421244, no es permitida
            | la conexión simultánea.""".trimMargin()
    )
}

fun getLoginUnassignedErrorUiModel(): ErrorUiModel {
    return ErrorUiModel(
        icon = "ic_ambulance",
        title = "No asignado",
        description = """"El dispositivo no se encuentra asociado al vehículo, debe comunicarse
            | con el Líder APH de la subred.""".trimMargin()
    )
}
