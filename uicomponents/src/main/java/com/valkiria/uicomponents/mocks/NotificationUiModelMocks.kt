package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.bricks.notification.NotificationUiModel

fun getAssignedIncidentNotificationUiModel(): NotificationUiModel {
    return NotificationUiModel(
        icon = "ic_assigned_incident",
        iconColor = "#3CF2DD",
        title = "Incidente Asignado",
        description = "CRU-12345678-22"
    )
}
