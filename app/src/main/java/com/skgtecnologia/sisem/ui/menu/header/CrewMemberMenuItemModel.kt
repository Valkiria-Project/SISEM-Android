package com.skgtecnologia.sisem.ui.menu.header

import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel

data class CrewMemberMenuItemModel(
    val name: String,
    val username: String,
    val specialtyAndDocument: String,
    val drawableProfession: String
)

fun AccessTokenModel.toCrewMemberItemModel() = CrewMemberMenuItemModel(
    name = nameUser,
    username = username,
    specialtyAndDocument = "${OperationRole.getRoleByName(role)?.humanizedName} $docType $document",
    drawableProfession = OperationRole.getRoleByName(role)?.humanizedName.orEmpty().toDrawable()
)

private fun String.toDrawable() = when (this) {
    "Auxiliar" -> "ic_aux"
    "Conductor" -> "ic_driver"
    "Médico" -> "ic_doctor"
    else -> "ic_lead"
}
