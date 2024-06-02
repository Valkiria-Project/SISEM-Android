package com.skgtecnologia.sisem.commons.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.location.usecases.UpdateLocation
import com.valkiria.uicomponents.utlis.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.io.File
import javax.inject.Inject

const val ACTION_START = "ACTION_START"
const val ACTION_STOP = "ACTION_STOP"
private const val CHANNEL_ID = "location"
private const val CHANNEL_NAME = "Location"
private const val LOCATION_INTERVAL = 5000L

private const val FILE_NAME = "android_location"

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var locationProvider: LocationProvider

    @Inject
    lateinit var updateLocation: UpdateLocation

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
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.location_tracking_title))
            .setContentText(getString(R.string.location_tracking_content))

        locationProvider
            .getLocationUpdates(LOCATION_INTERVAL)
            .catch { Timber.tag("Location").wtf(it.stackTraceToString()) }
            .onEach { location ->
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notificationBuilder.setContentText(
                    getString(R.string.location_tracking_content_update, lat, long)
                )

                Timber.tag("Location").d("locationProvider update")
                notificationManager.notify(1, updatedNotification.build())
                storeLocation(lat, long)
                updateLocation.invoke(location.latitude, location.longitude)
            }
            .launchIn(serviceScope)

        startForeground(1, notificationBuilder.build(), FOREGROUND_SERVICE_TYPE_LOCATION)
    }

    private fun storeLocation(lat: String, long: String) {
        val loc = TimeUtils.getLocalTimeAsString() + "\t" + lat + "\t" + long + "\n"
        File(filesDir, FILE_NAME).createNewFile()

        openFileOutput(FILE_NAME, Context.MODE_APPEND).use { output ->
            output.write(loc.toByteArray())
        }
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
