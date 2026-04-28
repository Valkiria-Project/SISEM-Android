package com.skgtecnologia.sisem.commons.location

import android.location.Location
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow

interface LocationProvider {

    fun getLocationUpdates(
        interval: Long,
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY
    ): Flow<Location>

    class LocationException(message: String) : Exception(message)
}
