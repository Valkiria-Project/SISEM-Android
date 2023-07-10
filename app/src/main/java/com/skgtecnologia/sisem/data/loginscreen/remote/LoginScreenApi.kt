package com.skgtecnologia.sisem.data.loginscreen.remote

import com.skgtecnologia.sisem.data.loginscreen.remote.model.LoginScreenBody
import com.skgtecnologia.sisem.data.loginscreen.remote.model.LoginScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginScreenApi {

    @POST("sisem-api/v1/screen/login")
    suspend fun getLoginScreen(@Body loginScreenBody: LoginScreenBody) :
        Response<LoginScreenResponse>
}
