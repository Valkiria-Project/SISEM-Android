package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED

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
