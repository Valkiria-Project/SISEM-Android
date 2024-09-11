package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.bricks.banner.BannerUiModel

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

fun getLoginUnassignedErrorUiModel(): BannerUiModel {
    return BannerUiModel(
        icon = "ic_ambulance",
        title = "No asignado",
        description = """"El dispositivo no se encuentra asociado al vehículo, debe comunicarse
            | con el Líder APH de la subred.
            """.trimMargin()
    )
}
