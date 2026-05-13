package com.skgtecnologia.sisem.domain.location.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.location.LocationRepository
import java.time.Instant
import javax.inject.Inject

class UpdateLocation @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        capturedAt: Instant,
    ): Result<Unit> = resultOf {
        locationRepository.updateLocation(latitude, longitude, capturedAt)
    }
}
