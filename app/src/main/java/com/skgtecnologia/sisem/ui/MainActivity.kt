package com.skgtecnologia.sisem.ui

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.commons.location.ACTION_START
import com.skgtecnologia.sisem.commons.location.ACTION_STOP
import com.skgtecnologia.sisem.commons.location.LocationService
import com.skgtecnologia.sisem.data.notification.NotificationsManager
import com.skgtecnologia.sisem.domain.notification.usecases.StoreNotification
import com.skgtecnologia.sisem.ui.navigation.SisemNavGraph
import com.skgtecnologia.sisem.ui.navigation.StartupNavigationModel
import com.skgtecnologia.sisem.ui.theme.SisemTheme
import com.valkiria.uicomponents.bricks.notification.model.getNotificationDataByType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val STARTUP_NAVIGATION_MODEL = "STARTUP_NAVIGATION_MODEL"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var storeNotification: StoreNotification

    @Inject
    lateinit var notificationsManager: NotificationsManager

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

        // Prevent Screenshots on all build types but debug
        if (BuildConfig.BUILD_TYPE != "debug") {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        val startupNavigationModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                STARTUP_NAVIGATION_MODEL, StartupNavigationModel::class.java
            )
        } else {
            intent.getParcelableExtra(STARTUP_NAVIGATION_MODEL)
        }

        MapboxNavigationApp.current()?.registerLocationObserver(locationObserver)
        Intent(applicationContext, LocationService::class.java).apply {
            action = ACTION_START
            startService(this)
        }

        intent.extras?.also { bundle ->
            handlePushNotification(bundle)
        }

        setContent {
            SisemTheme {
                SisemNavGraph(startupNavigationModel)
            }
        }
    }

    @Suppress("MagicNumber")
    private fun handlePushNotification(bundle: Bundle) {
        Timber.d("handlePushNotification")

        lifecycleScope.launch {
            getNotificationDataByType(bundle)?.also { notificationData ->
                storeNotification.invoke(notificationData)
                delay(500)
                NotificationEventHandler.publishNotificationEvent(notificationData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        MapboxNavigationApp.current()?.unregisterLocationObserver(locationObserver)
        Intent(applicationContext, LocationService::class.java).apply {
            action = ACTION_STOP
            startService(this)
        }
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
