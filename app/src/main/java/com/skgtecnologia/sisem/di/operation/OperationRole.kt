package com.skgtecnologia.sisem.di.operation

import java.util.Locale

enum class OperationRole(val humanizedName: String) {
    AUXILIARY_AND_OR_TAPH("Auxiliar"),
    DRIVER("Conductor"),
    LEAD_APH("Líder APH"),
    MEDIC_APH("Médico");

    companion object {
        fun getRoleByName(role: String): OperationRole? = entries.firstOrNull {
            it.name == role.uppercase(Locale.ROOT)
        }
    }
}
