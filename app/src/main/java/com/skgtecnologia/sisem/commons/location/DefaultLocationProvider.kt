package com.skgtecnologia.sisem.commons.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.HandlerThread
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.skgtecnologia.sisem.commons.extensions.hasMapLocationPermission
import com.skgtecnologia.sisem.commons.extensions.validateOrThrow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DefaultLocationProvider(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(
        interval: Long,
        priority: Int
    ): Flow<Location> {
        return callbackFlow {
            validateOrThrow(context.hasMapLocationPermission()) {
                Timber.tag("Location").d("Missing location permissions")
                LocationProvider.LocationException("Missing location permissions")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            validateOrThrow(isGpsEnabled && isNetworkEnabled) {
                LocationProvider.LocationException("GPS is disabled")
            }

            val request = LocationRequest.Builder(interval)
                .setMinUpdateIntervalMillis(interval)
                .setMaxUpdateDelayMillis(interval)
                .setPriority(priority)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    Timber.tag("Location").d("Location result: ${result.lastLocation}")
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            val handlerThread = HandlerThread("LocationCb").also { it.start() }

            client.requestLocationUpdates(
                request,
                locationCallback,
                handlerThread.looper
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
                handlerThread.quitSafely()
            }
        }
    }
}
