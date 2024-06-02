package com.skgtecnologia.sisem.ui.commons.extensions

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val UPDATE_INTERVAL_MINUTES = 2L
private const val FASTEST_UPDATE_INTERVAL_MINUTES = 1L
private const val MIN_UPDATE_DISTANCE_METERS = 30F

@Suppress("MissingPermission")
fun FusedLocationProviderClient.locationFlow() = callbackFlow<Location> {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        TimeUnit.MINUTES.toMillis(UPDATE_INTERVAL_MINUTES)
    )
        .setMinUpdateIntervalMillis(TimeUnit.MINUTES.toMillis(FASTEST_UPDATE_INTERVAL_MINUTES))
        .setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
        .build()

    @Suppress("TooGenericExceptionCaught")
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                try {
                    trySend(location).isSuccess
                } catch (t: Throwable) {
                    Timber.tag("Location").wtf(t, "Location couldn't be sent to the flow")
                }
            }
        }
    }

    requestLocationUpdates(
        locationRequest,
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        close(e)
    }

    awaitClose {
        removeLocationUpdates(callback)
    }
}
