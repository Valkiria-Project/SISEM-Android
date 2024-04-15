package com.skgtecnologia.sisem.commons.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.skgtecnologia.sisem.commons.extensions.validateOrThrow
import com.skgtecnologia.sisem.commons.extensions.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import timber.log.Timber

private const val MIN_UPDATE_DISTANCE_METERS = 50f

class DefaultLocationProvider(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            validateOrThrow(context.hasLocationPermission()) {
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
                .setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    Timber.d("Location result: ${result.lastLocation}")
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}
