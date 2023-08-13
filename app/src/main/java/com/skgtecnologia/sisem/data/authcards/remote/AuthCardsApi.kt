package com.skgtecnologia.sisem.data.authcards.remote

import com.skgtecnologia.sisem.data.operation.remote.model.OperationResponse
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthCardsApi {

    @GET("config")
    suspend fun getConfig(): Response<OperationResponse>

    @POST("screen/crewList")
    suspend fun getAuthCardsScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
