package com.skgtecnologia.sisem.domain.operation.model

import java.util.Locale

enum class PreoperationalStatus(val value: String) {
    SI("Si"),
    NO("No"),
    NO_APLICA("No Aplica");

    companion object {
        fun getStatusByName(status: String): PreoperationalStatus =
            PreoperationalStatus.entries.firstOrNull {
                it.name == status.uppercase(Locale.ROOT)
            } ?: SI
    }
}
