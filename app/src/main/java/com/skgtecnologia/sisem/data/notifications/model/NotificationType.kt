package com.skgtecnologia.sisem.data.notifications.model

enum class NotificationType(val title: String) {
    INCIDENT_ASSIGNED("Incidente Asignado");

    companion object {
        fun from(type: String): NotificationType ? = entries.firstOrNull { it.title == type }
    }
}
