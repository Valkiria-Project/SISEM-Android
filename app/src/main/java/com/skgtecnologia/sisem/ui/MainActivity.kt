package com.skgtecnologia.sisem.ui

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.skgtecnologia.sisem.ui.navigation.SisemNavGraph
import com.skgtecnologia.sisem.ui.navigation.StartupNavigationModel
import com.skgtecnologia.sisem.ui.theme.SisemTheme
import dagger.hilt.android.AndroidEntryPoint

const val STARTUP_NAVIGATION_MODEL = "STARTUP_NAVIGATION_MODEL"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var lastLocation: Location? = null

    private val locationObserver = object : LocationObserver {
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            lastLocation = locationMatcherResult.enhancedLocation
        }

        override fun onNewRawLocation(rawLocation: Location) {
            // no impl
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startupNavigationModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                STARTUP_NAVIGATION_MODEL, StartupNavigationModel::class.java
            )
        } else {
            intent.getParcelableExtra(STARTUP_NAVIGATION_MODEL)
        }

        MapboxNavigationApp.current()?.registerLocationObserver(locationObserver)

        setContent {
            SisemTheme {
                SisemNavGraph(startupNavigationModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        MapboxNavigationApp.current()?.unregisterLocationObserver(locationObserver)
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
