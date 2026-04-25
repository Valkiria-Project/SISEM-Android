package com.skgtecnologia.sisem.domain.location

interface LocationRepository {

    suspend fun updateLocation(latitude: Double, longitude: Double)
}
