package com.skgtecnologia.sisem.ui.commons.extensions

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val TabletWidth = 500.dp

@Composable
fun Activity.isTablet(): Boolean {
    val windowSizeClass = calculateWindowSizeClass(this)
    return windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
}

fun Context.getWindowHeight() =
    (this as? ComponentActivity)?.windowManager?.currentWindowMetrics?.bounds?.height()

fun Context.getWindowWidth() =
    (this as? ComponentActivity)?.windowManager?.currentWindowMetrics?.bounds?.width()
