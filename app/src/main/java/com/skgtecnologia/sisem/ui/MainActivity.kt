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
import com.valkiria.uicomponents.bricks.notification.model.IncidentAssignedNotification
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalTime

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

        handlePushNotification()

        setContent {
            SisemTheme {
                SisemNavGraph(startupNavigationModel)
            }
        }
    }

    private fun handlePushNotification() {
        Timber.d("handlePushNotification")
        intent.extras?.also {
            Timber.d("Background notification ${it.getString("incident_number")}")
            Timber.d("Background notification ${it.getString("incident_date")}")

            val notificationData = IncidentAssignedNotification(
                time = LocalTime.now(),
                cru = it.getString("CRU").orEmpty(),
                incidentNumber = it.getString("incident_number").orEmpty(),
                incidentType = it.getString("incident_date").orEmpty(),
                incidentPriority = it.getString("incident_priority").orEmpty(),
                incidentDate = it.getString("incident_date").orEmpty(),
                address = it.getString("address").orEmpty(),
                hour = it.getString("hour").orEmpty(),
                geolocation = it.getString("geolocation").orEmpty()
            )

            Timber.d("Background notification $notificationData")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        MapboxNavigationApp.current()?.unregisterLocationObserver(locationObserver)
    }

    companion object {
        fun launchMainActivity(
            packageContext: Context,
            model: StartupNavigationModel?,
            bundle: Bundle?
        ) {
            val intent = Intent(packageContext, MainActivity::class.java).apply {
                putExtra(STARTUP_NAVIGATION_MODEL, model)
                bundle?.let { putExtras(it) }
            }

            packageContext.startActivity(intent)
        }
    }
}
