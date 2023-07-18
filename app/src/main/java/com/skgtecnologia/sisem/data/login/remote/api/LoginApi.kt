package com.skgtecnologia.sisem.data.login.remote.api

import com.skgtecnologia.sisem.data.login.remote.model.LoginBody
import com.skgtecnologia.sisem.data.login.remote.model.LoginResponse
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("sisem-api/v1/screen/login")
    suspend fun getLoginScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("sisem-api/auth")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>
}
