package com.skgtecnologia.sisem.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.skgtecnologia.sisem.ui.navigation.SisemNavGraph
import com.skgtecnologia.sisem.ui.navigation.model.StartupNavigationModel
import com.skgtecnologia.sisem.ui.theme.SisemTheme
import dagger.hilt.android.AndroidEntryPoint

const val STARTUP_NAVIGATION_MODEL = "STARTUP_NAVIGATION_MODEL"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startupNavigationModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                STARTUP_NAVIGATION_MODEL, StartupNavigationModel::class.java
            )
        } else {
            intent.getParcelableExtra(STARTUP_NAVIGATION_MODEL)
        }

        setContent {
            SisemTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val isTablet = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

                SisemNavGraph(startupNavigationModel, isTablet)
            }
        }
    }

    companion object {
        fun launchMainActivity(packageContext: Context, model: StartupNavigationModel?) {
            val intent = Intent(packageContext, MainActivity::class.java).apply {
                putExtra(STARTUP_NAVIGATION_MODEL, model)
            }

            packageContext.startActivity(intent)
        }
    }
}
