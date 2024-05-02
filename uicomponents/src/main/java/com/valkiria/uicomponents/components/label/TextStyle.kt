package com.valkiria.uicomponents.components.label

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

enum class TextStyle {
    BODY_1,
    BODY_2,
    BUTTON_1,
    BUTTON_2,
    HEADLINE_1,
    HEADLINE_2,
    HEADLINE_3,
    HEADLINE_4,
    HEADLINE_5,
    HEADLINE_6,
    HEADLINE_7,
    HEADLINE_8;
}

@Composable
fun TextStyle?.toTextStyle() = when (this) {
    TextStyle.HEADLINE_1 -> MaterialTheme.typography.displayLarge
    TextStyle.HEADLINE_2 -> MaterialTheme.typography.displayMedium
    TextStyle.HEADLINE_3 -> MaterialTheme.typography.displaySmall
    TextStyle.HEADLINE_4 -> MaterialTheme.typography.headlineMedium
    TextStyle.HEADLINE_5 -> MaterialTheme.typography.headlineSmall
    TextStyle.HEADLINE_6 -> MaterialTheme.typography.titleLarge
    TextStyle.HEADLINE_7 -> MaterialTheme.typography.titleMedium
    TextStyle.HEADLINE_8 -> MaterialTheme.typography.titleSmall
    TextStyle.BODY_1 -> MaterialTheme.typography.bodyLarge
    TextStyle.BODY_2 -> MaterialTheme.typography.bodyMedium
    TextStyle.BUTTON_1 -> MaterialTheme.typography.labelLarge
    TextStyle.BUTTON_2 -> MaterialTheme.typography.labelMedium
    else -> MaterialTheme.typography.bodyLarge
}
