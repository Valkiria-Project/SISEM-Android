package com.skgtecnologia.sisem.data.notifications.model

const val NOTIFICATION_TYPE_KEY = "notification_type"

data class NotificationData(
    val data: Map<String, String>?
)
