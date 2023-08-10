package com.skgtecnologia.sisem.ui.menu.header

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel

data class CrewMemberItemModel(
    val name: String,
    val username: String,
    val specialty: String,
    val drawableProfession: String
)

fun AccessTokenModel.toCrewMemberItemModel() = CrewMemberItemModel(
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
