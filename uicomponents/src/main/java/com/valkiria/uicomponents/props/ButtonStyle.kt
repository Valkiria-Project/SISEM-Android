package com.valkiria.uicomponents.props

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ButtonStyle {
    LOUD,
    TRANSPARENT
}

@Composable
fun ButtonStyle.mapToColors(): ButtonColors = when (this) {
    ButtonStyle.LOUD -> ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
    )

    ButtonStyle.TRANSPARENT -> ButtonDefaults.buttonColors(
        containerColor = Color.Transparent
    )
}

@Composable
fun ButtonStyle.mapToTextColor(): Color = when (this) {
    ButtonStyle.LOUD -> MaterialTheme.colorScheme.onPrimary
    ButtonStyle.TRANSPARENT -> MaterialTheme.colorScheme.onPrimary
}
