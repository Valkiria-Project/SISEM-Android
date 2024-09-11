package com.valkiria.uicomponents.components.incident.model

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color

enum class IncidentPriority(
    val priorityName: String,
    val stringColor: String,
    val color: Color
) {
    CRITIC(
        "critica",
        "#DB2F5D",
        Color(parseColor("#DB2F5D"))
    ),
    HIGH(
        "alta",
        "#F55757",
        Color(parseColor("#F55757"))
    ),
    MEDIUM(
        "media",
        "#FC9125",
        Color(parseColor("#FC9125"))
    ),
    LOW(
        "baja",
        "#3CF2DD",
        Color(parseColor("#3CF2DD"))
    );

    companion object {
        fun getPriority(identifier: String): IncidentPriority = when (identifier.lowercase()) {
            CRITIC.priorityName -> CRITIC
            HIGH.priorityName -> HIGH
            MEDIUM.priorityName -> MEDIUM
            else -> LOW
        }
    }
}
