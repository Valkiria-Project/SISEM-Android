package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel

data class DrawerItemInfoPersonal(
    val name: String,
    val username: String,
    val specialty: String,
    val drawableProfession: String
)

fun AccessTokenModel.toDrawerItemInfoPersonal() = DrawerItemInfoPersonal(
    name = nameUser,
    username = username,
    specialty = role,
    drawableProfession = role.toDrawable()
)

private fun String.toDrawable() = when (this) {
    "MEDICO" -> "ic_doctor"
    "CONDUCTOR" -> "ic_driver"
    else -> "ic_aux"
}
