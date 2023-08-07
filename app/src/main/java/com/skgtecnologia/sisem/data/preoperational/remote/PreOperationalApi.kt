package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PreOperationalApi {

    @POST("https://run.mocky.io/v3/a3f734f0-8f93-4b3b-8310-e5302718976d")
    suspend fun getPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>
}
