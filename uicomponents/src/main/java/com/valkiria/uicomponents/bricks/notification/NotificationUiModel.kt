package com.valkiria.uicomponents.bricks.notification

import java.util.UUID

data class NotificationUiModel(
    val identifier: String = UUID.randomUUID().toString(),
    val icon: String,
    val iconColor: String,
    val title: String,
    val description: String,
    val location: String,
    val time: String
)
