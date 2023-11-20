package com.skgtecnologia.sisem.data.incident.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import javax.inject.Inject
import timber.log.Timber

class IncidentRemoteDataSource @Inject constructor(
    private val incidentApi: IncidentApi
) {

    suspend fun getIncidentInfo(
        idIncident: String,
        idTurn: String,
        codeVehicle: String
    ): Result<Unit> = apiCall {
        incidentApi.getIncidentInfo(
            idIncident = idIncident,
            idTurn = idTurn,
            codeVehicle = codeVehicle
        )
    }.mapResult {
        Timber.d("map to Incident domain model ${it.incident.code}")
//        it.mapToDomain()
    }
}
