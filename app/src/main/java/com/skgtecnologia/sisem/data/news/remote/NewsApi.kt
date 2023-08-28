package com.skgtecnologia.sisem.data.news.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NewsApi {

    @POST("screen/addReport")
    suspend fun getNewsScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
