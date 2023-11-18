package com.skgtecnologia.sisem.domain.notification.model

enum class NotificationType(val title: String) {
    INCIDENT_ASSIGNED("Incidente Asignado");

    companion object {
        fun from(type: String): NotificationType? = entries.firstOrNull { it.name == type }
    }
}
