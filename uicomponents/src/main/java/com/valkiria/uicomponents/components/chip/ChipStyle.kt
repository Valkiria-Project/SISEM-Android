package com.valkiria.uicomponents.components.chip

import android.graphics.Color.parseColor
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.AssistChipDefaults.assistChipBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private const val SECONDARY_CONTAINER_COLOR = "#3D3F42"

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
        containerColor = Color(parseColor(SECONDARY_CONTAINER_COLOR)),
        labelColor = Color.White,
        leadingIconContentColor = Color.White
    )
}

@Composable
fun ChipStyle.toChipBorder() = when (this) {
    ChipStyle.PRIMARY -> assistChipBorder(
        true,
        borderColor = MaterialTheme.colorScheme.primary
    )

    ChipStyle.SECONDARY -> assistChipBorder(
        true,
        borderColor = Color(parseColor(SECONDARY_CONTAINER_COLOR))
    )
}
