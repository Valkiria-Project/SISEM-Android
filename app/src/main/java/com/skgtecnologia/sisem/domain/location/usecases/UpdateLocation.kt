package com.skgtecnologia.sisem.domain.location.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.location.LocationRepository
import javax.inject.Inject

class UpdateLocation @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(): Result<Unit> = resultOf {
        locationRepository.updateLocation()
    }
}
