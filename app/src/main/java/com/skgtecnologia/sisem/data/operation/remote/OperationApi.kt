package com.skgtecnologia.sisem.data.operation.remote

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.auth.remote.model.LogoutResponse
import com.skgtecnologia.sisem.data.operation.remote.model.OperationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OperationApi {

    @GET("config")
    suspend fun getConfig(
        @Query("serial") serial: String,
        @Query("idTurn") idTurn: String? = null
    ): Response<OperationResponse>

    @GET(BuildConfig.AUTH_BASE_URL.plus("auth/logout"))
    suspend fun logoutTurn(
        @Query("idTurn") idTurn: String,
        @Query("idEmployed") idEmployed: String,
        @Query("vehicleCode") vehicleCode: String
    ): Response<LogoutResponse>
}
