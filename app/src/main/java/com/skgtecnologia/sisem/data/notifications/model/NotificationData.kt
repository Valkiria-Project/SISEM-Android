package com.skgtecnologia.sisem.data.notifications.model

import com.skgtecnologia.sisem.data.notifications.model.NotificationType.INCIDENT_ASSIGNED

const val NOTIFICATION_TYPE_KEY = "notification_type"

sealed interface NotificationData {
    val notificationType: NotificationType
}

data class IncidentAssignedNotification(
    override val notificationType: NotificationType = INCIDENT_ASSIGNED,
    val data: Map<String, String>?
) : NotificationData

enum class NotificationType(val title: String) {
    INCIDENT_ASSIGNED("Incidente Asignado");

    companion object {
        fun from(type: String): NotificationType? = entries.firstOrNull { it.title == type }
    }
}
