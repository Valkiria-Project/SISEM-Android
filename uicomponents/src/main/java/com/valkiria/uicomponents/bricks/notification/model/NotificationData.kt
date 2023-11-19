package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.IPS_PATIENT_TRANSFERRED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.NO_PRE_OPERATIONAL_GENERATED_CRUE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
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

// TRANSMILENIO_AUTHORIZATION
private const val AUTHORIZATION_NUMBER = "authorization_number"
private const val AUTHORIZES = "authorizes"

// SUPPORT_REQUEST_ON_THE_WAY
private const val RESOURCE_TYPE_AND_CODE = "resource_type_and_code"

// IPS_PATIENT_TRANSFERRED
private const val HEADQUARTERS_NAME = "headquarters_name"
private const val HEADQUARTERS_ADDRESS = "headquarters_address"


sealed interface NotificationData {
    val notificationType: NotificationType
}

fun getNotificationDataByType(notificationDataMap: Map<String, String>): NotificationData? {
    val notificationType = NotificationType.from(
        notificationDataMap[NOTIFICATION_TYPE_KEY].orEmpty()
    )
    Timber.d("notificationType ${notificationType?.title}")

    return when (notificationType) {
        INCIDENT_ASSIGNED -> IncidentAssignedNotification(
            incidentNumber = notificationDataMap[INCIDENT_NUMBER].orEmpty(),
            incidentType = notificationDataMap[INCIDENT_TYPE].orEmpty(),
            incidentPriority = notificationDataMap[INCIDENT_PRIORITY].orEmpty(),
            incidentDate = notificationDataMap[INCIDENT_DATE].orEmpty(),
            address = notificationDataMap[ADDRESS].orEmpty(),
            hour = notificationDataMap[HOUR].orEmpty(),
            geolocation = notificationDataMap[GEOLOCATION].orEmpty()
        )

        TRANSMILENIO_AUTHORIZATION -> TransmilenioAuthorizationNotification(
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER].orEmpty(),
            authorizes = notificationDataMap[AUTHORIZES].orEmpty()
        )

        TRANSMILENIO_DENIED -> TransmilenioDeniedNotification()

        NO_PRE_OPERATIONAL_GENERATED_CRUE -> NoPreOperationalGeneratedCrueNotification()

        SUPPORT_REQUEST_ON_THE_WAY -> SupportRequestNotification(
            resourceTypeCode = notificationDataMap[RESOURCE_TYPE_AND_CODE].orEmpty()
        )

        IPS_PATIENT_TRANSFERRED -> IpsPatientTransferredNotification(
            headquartersName = notificationDataMap[HEADQUARTERS_NAME].orEmpty(),
            headquartersAddress = notificationDataMap[HEADQUARTERS_ADDRESS].orEmpty()
        )

        else -> null
    }
}

fun getNotificationRawDataByType(notificationData: NotificationData): Map<String, String> {
    return when (notificationData) {
        is IncidentAssignedNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            INCIDENT_NUMBER to notificationData.incidentNumber,
            INCIDENT_NUMBER to notificationData.incidentNumber,
            INCIDENT_TYPE to notificationData.incidentType,
            INCIDENT_PRIORITY to notificationData.incidentPriority,
            INCIDENT_DATE to notificationData.incidentDate,
            ADDRESS to notificationData.address,
            HOUR to notificationData.hour,
            GEOLOCATION to notificationData.geolocation
        )

        is TransmilenioAuthorizationNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            AUTHORIZATION_NUMBER to notificationData.authorizationNumber,
            AUTHORIZES to notificationData.authorizes
        )

        is TransmilenioDeniedNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name
        )

        is NoPreOperationalGeneratedCrueNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name
        )

        is SupportRequestNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            RESOURCE_TYPE_AND_CODE to notificationData.resourceTypeCode
        )

        is IpsPatientTransferredNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            HEADQUARTERS_NAME to notificationData.headquartersName,
            HEADQUARTERS_ADDRESS to notificationData.headquartersAddress
        )
    }
}
