package com.valkiria.uicomponents.bricks.notification.model

import timber.log.Timber

// TRANSMILENIO_AUTHORIZATION
const val AUTHORIZATION_NUMBER = "authorization_number"
const val AUTHORIZES = "authorizes"
const val JOURNEY = "journey"

// TRANSMILENIO_DENIED
const val AUTHORIZATION_NUMBER_DENIED = "authorization_number"

interface TransmiNotification : NotificationData

fun getTransmiNotificationDataByType(
    notificationDataMap: Map<String, String>
): TransmiNotification {
    val notificationType = NotificationType.from(
        notificationDataMap[NOTIFICATION_TYPE_KEY].orEmpty()
    )
    Timber.d("transmiNotificationType ${notificationType?.title}")

    return when (notificationType) {
        NotificationType.TRANSMILENIO_AUTHORIZATION -> TransmilenioAuthorizationNotification(
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER].orEmpty(),
            authorizes = notificationDataMap[AUTHORIZES].orEmpty(),
            journey = notificationDataMap[JOURNEY].orEmpty()
        )

        NotificationType.TRANSMILENIO_DENIED -> TransmilenioDeniedNotification(
            authorizationNumber = notificationDataMap[AUTHORIZATION_NUMBER_DENIED].orEmpty()
        )

        else -> error("Invalid Transmi Notification Type")
    }
}

fun getTransmiNotificationRawDataByType(
    transmiNotification: TransmiNotification
): Map<String, String> {
    return when (transmiNotification) {
        is TransmilenioAuthorizationNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to transmiNotification.notificationType.name,
            AUTHORIZATION_NUMBER to transmiNotification.authorizationNumber,
            AUTHORIZES to transmiNotification.authorizes,
            JOURNEY to transmiNotification.journey
        )

        is TransmilenioDeniedNotification -> mapOf(
            NOTIFICATION_TYPE_KEY to transmiNotification.notificationType.name,
            AUTHORIZATION_NUMBER_DENIED to transmiNotification.authorizationNumber
        )

        else -> error("Invalid Transmi Notification Type")
    }
}