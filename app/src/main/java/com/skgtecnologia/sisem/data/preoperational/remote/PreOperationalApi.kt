package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PreOperationalApi {

    @POST("https://run.mocky.io/v3/926ff8d0-70ba-4307-aa10-66522adc341f") // FIXME
    suspend fun getPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("https://run.mocky.io/v3/e94e0473-9c06-4590-9060-d6b551389542") // FIXME
    suspend fun sendPreOperational(
        @Body screenBody: ScreenBody
    ): Response<Unit>
}
