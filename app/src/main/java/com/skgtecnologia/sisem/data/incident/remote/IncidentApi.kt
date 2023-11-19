package com.skgtecnologia.sisem.data.incident.remote

import com.skgtecnologia.sisem.data.auth.remote.model.LogoutResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IncidentApi {

    @GET("incident/incident-code/{idIncident}/{idTurn}")
    suspend fun getIncidentInfo(
        @Path("idIncident") idIncident: String,
        @Path("idTurn") idTurn: String,
    ): Response<LogoutResponse>
}
