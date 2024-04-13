package com.skgtecnologia.sisem.data.location

import com.skgtecnologia.sisem.data.location.remote.LocationRemoteDataSource
import com.skgtecnologia.sisem.domain.location.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationRemoteDataSource: LocationRemoteDataSource
) : LocationRepository {

    override suspend fun updateLocation() {
        locationRemoteDataSource.sendUpdatedLocation(
            idVehicle = "0450",
            idDevice = "AHK2345LZ",
            idOrigin = 2,
            latitude = 4.6608373,
            longitude = -74.1101529,
            originAt = "2023-07-20T10:13:34.832563"
        )
    }
}
