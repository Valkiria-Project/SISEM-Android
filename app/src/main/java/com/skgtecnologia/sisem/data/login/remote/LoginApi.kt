package com.skgtecnologia.sisem.data.login.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("v1/screen/login")
    suspend fun getLoginScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
