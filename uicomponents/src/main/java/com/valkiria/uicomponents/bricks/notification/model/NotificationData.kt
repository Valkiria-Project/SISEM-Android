package com.valkiria.uicomponents.bricks.notification.model

import com.valkiria.uicomponents.bricks.notification.model.NotificationType.CLOSING_OF_APH
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.IPS_PATIENT_TRANSFERRED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.NO_PRE_OPERATIONAL_GENERATED_CRUE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.STRETCHER_RETENTION_ENABLE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
import timber.log.Timber
import java.time.LocalTime

// NOTIFICATION_TYPE
const val NOTIFICATION_TYPE_KEY = "notification_type"

// INCIDENT_ASSIGNED
private const val CRU = "CRU"
private const val INCIDENT_NUMBER = "incident_number"
private const val INCIDENT_TYPE = "incident_type"
private const val INCIDENT_PRIORITY = "incident_priority"
private const val INCIDENT_DATE = "incident_date"
private const val ADDRESS = "address"
private const val HOUR = "hour"
private const val GEOLOCATION = "geolocation"

// SUPPORT_REQUEST_ON_THE_WAY
private const val RESOURCE_TYPE_AND_CODE = "resource_type_and_code"

// IPS_PATIENT_TRANSFERRED
private const val HEADQUARTERS_NAME = "headquarters_name"
private const val HEADQUARTERS_ADDRESS = "headquarters_address"

// CLOSING_OF_APH
private const val CONSECUTIVE_NUMBER = "consecutive_number"
private const val UPDATE_TIME_OBSERVATIONS = "update_time_observations_and_attachments"

sealed interface NotificationData {
    val notificationType: NotificationType
    val time: LocalTime
}

fun getNotificationDataByType(
    notificationDataMap: Map<String, String>,
    time: LocalTime? = null
): NotificationData? {
    val notificationType = NotificationType.from(
        notificationDataMap[NOTIFICATION_TYPE_KEY].orEmpty()
    )
    Timber.d("notificationType ${notificationType?.title}")

    val notificationTime = time ?: LocalTime.now()

    return when (notificationType) {
        INCIDENT_ASSIGNED -> IncidentAssignedNotification(
            time = notificationTime,
            cru = notificationDataMap[CRU].orEmpty(),
            incidentNumber = notificationDataMap[INCIDENT_NUMBER].orEmpty(),
            incidentType = notificationDataMap[INCIDENT_TYPE].orEmpty(),
            incidentPriority = notificationDataMap[INCIDENT_PRIORITY].orEmpty(),
            incidentDate = notificationDataMap[INCIDENT_DATE].orEmpty(),
            address = notificationDataMap[ADDRESS].orEmpty(),
            hour = notificationDataMap[HOUR].orEmpty(),
            geolocation = notificationDataMap[GEOLOCATION].orEmpty()
        )

        TRANSMILENIO_AUTHORIZATION -> TransmilenioAuthorizationNotification(
            time = notificationTime,
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER].orEmpty(),
            authorizes = notificationDataMap[AUTHORIZES].orEmpty(),
            journey = notificationDataMap[JOURNEY].orEmpty()
        )

        TRANSMILENIO_DENIED -> TransmilenioDeniedNotification(
            time = notificationTime,
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER_DENIED].orEmpty()
        )

        NO_PRE_OPERATIONAL_GENERATED_CRUE -> NoPreOperationalGeneratedCrueNotification(
            time = notificationTime
        )

        SUPPORT_REQUEST_ON_THE_WAY -> SupportRequestNotification(
            time = notificationTime,
            resourceTypeCode = notificationDataMap[RESOURCE_TYPE_AND_CODE].orEmpty()
        )

        IPS_PATIENT_TRANSFERRED -> IpsPatientTransferredNotification(
            time = notificationTime,
            headquartersName = notificationDataMap[HEADQUARTERS_NAME].orEmpty(),
            headquartersAddress = notificationDataMap[HEADQUARTERS_ADDRESS].orEmpty()
        )

        STRETCHER_RETENTION_ENABLE -> StretcherRetentionEnableNotification(
            time = notificationTime
        )

        CLOSING_OF_APH -> ClosingAPHNotification(
            time = notificationTime,
            consecutiveNumber = notificationDataMap[CONSECUTIVE_NUMBER].orEmpty(),
            updateTimeObservationsAttachments = notificationDataMap[UPDATE_TIME_OBSERVATIONS]
                .orEmpty()
        )

        else -> null
    }
}

fun getNotificationRawDataByType(notificationData: NotificationData): Map<String, String> {
    return when (notificationData) {
        is IncidentAssignedNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            CRU to notificationData.cru,
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
            AUTHORIZES to notificationData.authorizes,
            JOURNEY to notificationData.journey
        )

        is TransmilenioDeniedNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            AUTHORIZATION_NUMBER_DENIED to notificationData.authorizationNumber
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

        is StretcherRetentionEnableNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name
        )

        is ClosingAPHNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            CONSECUTIVE_NUMBER to notificationData.consecutiveNumber,
            UPDATE_TIME_OBSERVATIONS to notificationData.updateTimeObservationsAttachments
        )

        is TransmiNotification -> mapOf()
    }
}
