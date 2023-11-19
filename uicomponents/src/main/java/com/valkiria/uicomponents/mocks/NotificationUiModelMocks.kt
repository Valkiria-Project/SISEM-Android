package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.bricks.notification.NotificationUiModel

fun getAssignedIncidentNotificationUiModel(): NotificationUiModel {
    return NotificationUiModel(
        icon = "ic_assigned_incident",
        iconColor = "#3CF2DD",
        title = "Incidente Asignado",
        description = "CRU-12345678-22",
        contentLeft = "Kra 45 #43-21",
        contentRight = "11 Min"
    )
}

fun getTransmilenioAuthNotificationUiModel(): NotificationUiModel {
    return NotificationUiModel(
        icon = "ic_road",
        iconColor = "#3CF2DD",
        title = "Carril de Transmilenio autorizado",
        description = "No. 12345678-22",
        contentLeft = "Autoriza Juan Correa"
    )
}
