package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PreOperationalApi {

    @POST("https://run.mocky.io/v3/9ed433ca-b322-4a49-b4db-110209f1cb6a")
    suspend fun getPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>
}
