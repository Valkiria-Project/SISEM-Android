package com.skgtecnologia.sisem.data.incident.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.incident.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.domain.incident.model.IncidentModel
import javax.inject.Inject

class IncidentRemoteDataSource @Inject constructor(
    private val incidentApi: IncidentApi
) {

    suspend fun getIncidentInfo(
        idIncident: String,
        idTurn: String,
        codeVehicle: String
    ): Result<IncidentModel> = apiCall {
        incidentApi.getIncidentInfo(
            idIncident = idIncident,
            idTurn = idTurn,
            codeVehicle = codeVehicle
        )
    }.mapResult {
        it.mapToDomain()
    }
}
