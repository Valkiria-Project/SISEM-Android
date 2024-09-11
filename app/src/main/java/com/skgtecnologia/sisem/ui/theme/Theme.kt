@file:Suppress("PrivatePropertyName")

package com.skgtecnologia.sisem.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DodgerBlue,
    onPrimary = Color.Black,
    secondary = OuterSpace,
    tertiary = BrightTurquoise,
    background = Shark,
    onBackground = Color.White,
    error = Carnation,
    surface = Shark,
    onSurface = Color.White,
    onSurfaceVariant = Color.White
)

@Composable
fun SisemTheme(
    /*
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
     */
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
