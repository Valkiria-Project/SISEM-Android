package com.skgtecnologia.sisem.domain.notification.model

import com.skgtecnologia.sisem.domain.notification.model.NotificationType.INCIDENT_ASSIGNED

data class IncidentAssignedNotification(
    override val notificationType: NotificationType = INCIDENT_ASSIGNED,
    val incidentNumber: String,
    val incidentType: String,
    val incidentPriority: String,
    val incidentDate: String,
    val address: String,
    val hour: String,
    val geolocation: String
) : NotificationData
