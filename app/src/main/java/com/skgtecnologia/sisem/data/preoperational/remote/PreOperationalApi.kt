package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PreOperationalApi {

    @POST("https://run.mocky.io/v3/d24a9108-d2aa-4b66-8676-a4ddbc74ad9e") // BACKEND
    suspend fun getPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("https://run.mocky.io/v3/e94e0473-9c06-4590-9060-d6b551389542") // BACKEND
    suspend fun sendPreOperational(
        @Body screenBody: ScreenBody
    ): Response<Unit>
}
