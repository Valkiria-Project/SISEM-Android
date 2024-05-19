package com.valkiria.uicomponents.bricks.notification.model

import android.os.Bundle
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.AUTH_TIME
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.CLOSING_OF_APH
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.INCIDENT_ASSIGNED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.IPS_PATIENT_TRANSFERRED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.NO_PRE_OPERATIONAL_GENERATED_CRUE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.STRETCHER_RETENTION_ENABLE
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.SUPPORT_REQUEST_ON_THE_WAY
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_AUTHORIZATION
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.TRANSMILENIO_DENIED
import com.valkiria.uicomponents.bricks.notification.model.NotificationType.UPDATE_VEHICLE_STATUS
import timber.log.Timber
import java.time.LocalDateTime
import java.time.LocalTime

// NOTIFICATION_TYPE
const val NOTIFICATION_TYPE_KEY = "notification_type"

// INCIDENT_ASSIGNED
const val CRU = "CRU"
const val INCIDENT_NUMBER = "incident_number"
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
    val dateTime: LocalDateTime
}

@Suppress("LongMethod")
fun getNotificationDataByType(
    notificationDataMap: Map<String, String>,
    time: LocalTime? = null,
    dateTime: LocalDateTime? = null
): NotificationData? {
    val notificationType = NotificationType.from(
        notificationDataMap[NOTIFICATION_TYPE_KEY].orEmpty()
    )
    Timber.d("notificationType ${notificationType?.title}")

    val notificationTime = time ?: LocalTime.now()
    val notificationDateTime = dateTime ?: LocalDateTime.now()

    return when (notificationType) {
        AUTH_TIME -> AuthTimeNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        CLOSING_OF_APH -> ClosingAPHNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            consecutiveNumber = notificationDataMap[CONSECUTIVE_NUMBER].orEmpty(),
            updateTimeObservationsAttachments = notificationDataMap[UPDATE_TIME_OBSERVATIONS]
                .orEmpty()
        )

        INCIDENT_ASSIGNED -> IncidentAssignedNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            cru = notificationDataMap[CRU].orEmpty(),
            incidentNumber = notificationDataMap[INCIDENT_NUMBER].orEmpty(),
            incidentType = notificationDataMap[INCIDENT_TYPE].orEmpty(),
            incidentPriority = notificationDataMap[INCIDENT_PRIORITY].orEmpty(),
            incidentDate = notificationDataMap[INCIDENT_DATE].orEmpty(),
            address = notificationDataMap[ADDRESS].orEmpty(),
            hour = notificationDataMap[HOUR].orEmpty(),
            geolocation = notificationDataMap[GEOLOCATION].orEmpty()
        )

        IPS_PATIENT_TRANSFERRED -> IpsPatientTransferredNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            headquartersName = notificationDataMap[HEADQUARTERS_NAME].orEmpty(),
            headquartersAddress = notificationDataMap[HEADQUARTERS_ADDRESS].orEmpty(),
            geolocation = notificationDataMap[GEOLOCATION].orEmpty()
        )

        NO_PRE_OPERATIONAL_GENERATED_CRUE -> NoPreOperationalGeneratedCrueNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        SUPPORT_REQUEST_ON_THE_WAY -> SupportRequestNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            resourceTypeCode = notificationDataMap[RESOURCE_TYPE_AND_CODE].orEmpty()
        )

        STRETCHER_RETENTION_ENABLE -> StretcherRetentionEnableNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        TRANSMILENIO_AUTHORIZATION -> TransmilenioAuthorizationNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER].orEmpty(),
            authorizes = notificationDataMap[AUTHORIZES].orEmpty(),
            journey = notificationDataMap[JOURNEY].orEmpty()
        )

        TRANSMILENIO_DENIED -> TransmilenioDeniedNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER_DENIED].orEmpty()
        )

        UPDATE_VEHICLE_STATUS -> UpdateVehicleStatusNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        null -> null
    }
}

@Suppress("LongMethod")
fun getNotificationDataByType(
    notificationDataBundle: Bundle,
    time: LocalTime? = null,
    dateTime: LocalDateTime? = null
): NotificationData? {
    val notificationType = NotificationType.from(
        notificationDataBundle.getString(NOTIFICATION_TYPE_KEY).orEmpty()
    )
    Timber.d("notificationType ${notificationType?.title}")

    val notificationTime = time ?: LocalTime.now()
    val notificationDateTime = dateTime ?: LocalDateTime.now()

    return when (notificationType) {
        AUTH_TIME -> AuthTimeNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        CLOSING_OF_APH -> ClosingAPHNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            consecutiveNumber = notificationDataBundle.getString(CONSECUTIVE_NUMBER).orEmpty(),
            updateTimeObservationsAttachments = notificationDataBundle.getString(
                UPDATE_TIME_OBSERVATIONS
            ).orEmpty()
        )

        INCIDENT_ASSIGNED -> IncidentAssignedNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            cru = notificationDataBundle.getString(CRU).orEmpty(),
            incidentNumber = notificationDataBundle.getString(INCIDENT_NUMBER).orEmpty(),
            incidentType = notificationDataBundle.getString(INCIDENT_TYPE).orEmpty(),
            incidentPriority = notificationDataBundle.getString(INCIDENT_PRIORITY).orEmpty(),
            incidentDate = notificationDataBundle.getString(INCIDENT_DATE).orEmpty(),
            address = notificationDataBundle.getString(ADDRESS).orEmpty(),
            hour = notificationDataBundle.getString(HOUR).orEmpty(),
            geolocation = notificationDataBundle.getString(GEOLOCATION).orEmpty()
        )

        IPS_PATIENT_TRANSFERRED -> IpsPatientTransferredNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            headquartersName = notificationDataBundle.getString(HEADQUARTERS_NAME).orEmpty(),
            headquartersAddress = notificationDataBundle.getString(HEADQUARTERS_ADDRESS).orEmpty(),
            geolocation = notificationDataBundle.getString(GEOLOCATION).orEmpty()
        )

        NO_PRE_OPERATIONAL_GENERATED_CRUE -> NoPreOperationalGeneratedCrueNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        STRETCHER_RETENTION_ENABLE -> StretcherRetentionEnableNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        SUPPORT_REQUEST_ON_THE_WAY -> SupportRequestNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            resourceTypeCode = notificationDataBundle.getString(RESOURCE_TYPE_AND_CODE).orEmpty()
        )

        TRANSMILENIO_AUTHORIZATION -> TransmilenioAuthorizationNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            authorizationNumber = notificationDataBundle.getString(AUTHORIZATION_NUMBER).orEmpty(),
            authorizes = notificationDataBundle.getString(AUTHORIZES).orEmpty(),
            journey = notificationDataBundle.getString(JOURNEY).orEmpty()
        )

        TRANSMILENIO_DENIED -> TransmilenioDeniedNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
            authorizationNumber = notificationDataBundle.getString(AUTHORIZATION_NUMBER_DENIED)
                .orEmpty()
        )

        UPDATE_VEHICLE_STATUS -> UpdateVehicleStatusNotification(
            time = notificationTime,
            dateTime = notificationDateTime,
        )

        null -> null
    }
}

fun getNotificationRawDataByType(notificationData: NotificationData): Map<String, String> {
    return when (notificationData) {
        is AuthTimeNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
        )

        is ClosingAPHNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            CONSECUTIVE_NUMBER to notificationData.consecutiveNumber,
            UPDATE_TIME_OBSERVATIONS to notificationData.updateTimeObservationsAttachments
        )

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

        is IpsPatientTransferredNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            HEADQUARTERS_NAME to notificationData.headquartersName,
            HEADQUARTERS_ADDRESS to notificationData.headquartersAddress,
            GEOLOCATION to notificationData.geolocation.orEmpty()
        )

        is NoPreOperationalGeneratedCrueNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name
        )

        is StretcherRetentionEnableNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name
        )

        is SupportRequestNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
            RESOURCE_TYPE_AND_CODE to notificationData.resourceTypeCode
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

        is TransmiNotification -> mapOf()

        is UpdateVehicleStatusNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to notificationData.notificationType.name,
        )
    }
}
