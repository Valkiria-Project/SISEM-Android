package com.skgtecnologia.sisem.commons.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {

    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String) : Exception(message)
}
