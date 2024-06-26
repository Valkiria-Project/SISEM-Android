package com.skgtecnologia.sisem.data.location.remote

import com.skgtecnologia.sisem.data.location.remote.model.LocationBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import timber.log.Timber
import javax.inject.Inject

class LocationRemoteDataSource @Inject constructor(
    private val locationApi: LocationApi
) {

    @Suppress("LongParameterList")
    suspend fun sendUpdatedLocation(
        idVehicle: String,
        idDevice: String,
        idOrigin: Int,
        latitude: Double,
        longitude: Double,
        originAt: String,
        idIncident: String?
    ): Result<Unit> = apiCall {
        Timber.tag("Location").d("LocationRemoteDataSource updateLocation")
        locationApi.updateLocation(
            locationBody = LocationBody(
                idVehicle = idVehicle,
                idDevice = idDevice,
                idOrigin = idOrigin,
                latitude = latitude,
                longitude = longitude,
                originAt = originAt,
                idIncident = idIncident
            )
        )
    }
}
