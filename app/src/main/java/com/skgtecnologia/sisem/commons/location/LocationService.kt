package com.skgtecnologia.sisem.commons.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.skgtecnologia.sisem.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

const val ACTION_START = "ACTION_START"
const val ACTION_STOP = "ACTION_STOP"
private const val CHANNEL_ID = "location"
private const val CHANNEL_NAME = "Location"
private const val LOCATION_INTERVAL = 5000L

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    private lateinit var locationProvider: LocationProvider

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.location_tracking_title))
            .setContentText(getString(R.string.location_tracking_content))

        locationProvider
            .getLocationUpdates(LOCATION_INTERVAL)
            .catch { Timber.wtf(it.stackTraceToString()) }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notificationBuilder.setContentText(
                    getString(R.string.location_tracking_content_update, lat, long)
                )

                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notificationBuilder.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
