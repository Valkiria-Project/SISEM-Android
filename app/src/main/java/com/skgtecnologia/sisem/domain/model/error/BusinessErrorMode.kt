package com.skgtecnologia.sisem.domain.model.error

// FIXME: Handle this in the use Cases
fun changePasswordEmptyFields(): ErrorModel = ErrorModel(
    icon = "ic_alert",
    title = "Incompleto",
    description = "Para guardar el cambio de contraseña es necesario que complete todos los campos."
)

// FIXME: Handle this in the use Cases
fun changePasswordNoMatch(): ErrorModel = ErrorModel(
    icon = "ic_alert",
    title = "Nueva contraseña",
    description = "Nueva contraseña y confirmar nueva contraseña no coinciden."
)
