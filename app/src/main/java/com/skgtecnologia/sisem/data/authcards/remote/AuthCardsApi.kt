package com.skgtecnologia.sisem.data.authcards.remote

import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthCardsApi {

    @POST("screen/crewList")
    suspend fun getAuthCardsScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
