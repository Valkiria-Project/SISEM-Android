package com.skgtecnologia.sisem.domain.model.banner

// FIXME: Handle this in the use Cases
fun changePasswordEmptyFields(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Incompleto",
    description = "Para guardar el cambio de contraseña es necesario que complete todos los campos."
)

// FIXME: Handle this in the use Cases
fun changePasswordNoMatch(): BannerModel = BannerModel(
    icon = "ic_alert",
    title = "Nueva contraseña",
    description = "Nueva contraseña y confirmar nueva contraseña no coinciden."
)

// FIXME: Delete this with refactor de banner commponent
fun changePasswordSuccess(): BannerModel = BannerModel(
    icon = "ic_alert",
    iconColor = "#42A4FA",
    title = "Nueva contraseña",
    description = "La contraseña se ha cambiado con éxito."
)