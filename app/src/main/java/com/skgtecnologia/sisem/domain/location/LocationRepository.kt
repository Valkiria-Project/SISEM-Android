package com.skgtecnologia.sisem.domain.location

import java.time.Instant

interface LocationRepository {

    suspend fun updateLocation(
        latitude: Double,
        longitude: Double,
        capturedAt: Instant,
    )
}
