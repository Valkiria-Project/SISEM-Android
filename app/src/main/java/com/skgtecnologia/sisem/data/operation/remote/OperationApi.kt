package com.skgtecnologia.sisem.data.operation.remote

import com.skgtecnologia.sisem.data.auth.remote.model.LogoutResponse
import com.skgtecnologia.sisem.data.operation.remote.model.OperationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OperationApi {

    @GET("config")
    suspend fun getConfig(@Query("serial") serial: String): Response<OperationResponse>

    @GET("auth/logout")
    suspend fun logoutTurn(
        @Header("username") username: String,
        @Query("idTurn") idTurn: String,
        @Query("idEmployed") idEmployed: String,
        @Query("vehicleCode") vehicleCode: String
    ): Response<LogoutResponse>
}
