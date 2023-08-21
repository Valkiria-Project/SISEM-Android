package com.skgtecnologia.sisem.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.skgtecnologia.sisem.ui.navigation.SisemAppNavigation
import com.skgtecnologia.sisem.ui.theme.SisemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SisemTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val isTablet = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

                SisemAppNavigation(isTablet)
            }
        }
    }
}
