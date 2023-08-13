package com.skgtecnologia.sisem.data.operation.remote

import com.skgtecnologia.sisem.data.operation.remote.model.OperationResponse
import retrofit2.Response
import retrofit2.http.GET

interface OperationApi {

    @GET("config")
    suspend fun getConfig(): Response<OperationResponse>
}
