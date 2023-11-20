package com.skgtecnologia.sisem.data.incident.remote

import com.skgtecnologia.sisem.data.incident.remote.model.IncidentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IncidentApi {

    @GET("incident/incident-code/{idIncident}/turn/{idTurn}")
    suspend fun getIncidentInfo(
        @Path("idIncident") idIncident: String,
        @Path("idTurn") idTurn: String,
        @Query("codeVehicle") codeVehicle: String? = null
    ): Response<IncidentResponse>
}
