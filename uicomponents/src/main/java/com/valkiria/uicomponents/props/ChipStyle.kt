package com.valkiria.uicomponents.props

import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ChipStyle {
    PRIMARY,
    SECONDARY
}

@Composable
fun ChipStyle.toChipColors() = when (this) {
    ChipStyle.PRIMARY -> AssistChipDefaults.assistChipColors(
        containerColor = MaterialTheme.colorScheme.primary,
        labelColor = Color.Black,
        leadingIconContentColor = Color.Black
    )

    ChipStyle.SECONDARY -> AssistChipDefaults.assistChipColors(
        containerColor = MaterialTheme.colorScheme.background,
        labelColor = Color.White,
        leadingIconContentColor = Color.White
    )
}

@Composable
fun ChipStyle.toChipBorder() = when (this) {
    ChipStyle.PRIMARY -> AssistChipDefaults.assistChipBorder(
        borderColor = MaterialTheme.colorScheme.primary
    )

    ChipStyle.SECONDARY -> AssistChipDefaults.assistChipBorder(
        borderColor = MaterialTheme.colorScheme.background
    )
}
