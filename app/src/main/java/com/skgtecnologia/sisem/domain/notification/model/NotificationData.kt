package com.skgtecnologia.sisem.domain.notification.model

import timber.log.Timber

// NOTIFICATION_TYPE
private const val NOTIFICATION_TYPE_KEY = "notification_type"

// INCIDENT_ASSIGNED
private const val INCIDENT_NUMBER = "incident_number"
private const val INCIDENT_TYPE = "incident_type"
private const val INCIDENT_PRIORITY = "incident_priority"
private const val INCIDENT_DATE = "incident_date"
private const val ADDRESS = "address"
private const val HOUR = "hour"
private const val GEOLOCATION = "geolocation"

sealed interface NotificationData {
    val notificationType: NotificationType
}

fun getNotificationDataByType(notificationDataMap: Map<String, String>): NotificationData? {
    val notificationType = NotificationType.from(
        notificationDataMap[NOTIFICATION_TYPE_KEY].orEmpty()
    )
    Timber.d("notificationType ${notificationType?.title}")

    return when (notificationType) {
        NotificationType.INCIDENT_ASSIGNED -> {
            IncidentAssignedNotification(
                incidentNumber = notificationDataMap[INCIDENT_NUMBER].orEmpty(),
                incidentType = notificationDataMap[INCIDENT_TYPE].orEmpty(),
                incidentPriority = notificationDataMap[INCIDENT_PRIORITY].orEmpty(),
                incidentDate = notificationDataMap[INCIDENT_DATE].orEmpty(),
                address = notificationDataMap[ADDRESS].orEmpty(),
                hour = notificationDataMap[HOUR].orEmpty(),
                geolocation = notificationDataMap[GEOLOCATION].orEmpty()
            )
        }

        else -> null
    }
}

fun getNotificationRawDataByType(notificationData: NotificationData): Map<String, String> {
    return when (notificationData) {
        is IncidentAssignedNotification -> {
            mapOf(
                INCIDENT_NUMBER to notificationData.incidentNumber,
                INCIDENT_TYPE to notificationData.incidentType,
                INCIDENT_PRIORITY to notificationData.incidentPriority,
                INCIDENT_DATE to notificationData.incidentDate,
                ADDRESS to notificationData.address,
                HOUR to notificationData.hour,
                GEOLOCATION to notificationData.geolocation
            )
        }
    }
}
