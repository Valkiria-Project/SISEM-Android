package com.valkiria.uicomponents.bricks.notification

enum class NotificationType(val title: String) {
    INCIDENT_ASSIGNED("Incidente Asignado"),
    TRANSMILENIO_AUTHORIZATION("Carril de Transmilenio autorizado");

    companion object {
        fun from(type: String): NotificationType? = entries.firstOrNull { it.name == type }
    }
}
