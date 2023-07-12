package com.skgtecnologia.sisem.data.login.remote.api

import com.skgtecnologia.sisem.data.login.remote.model.LoginScreenBody
import com.skgtecnologia.sisem.data.login.remote.model.LoginScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("sisem-api/v1/screen/login")
    suspend fun getLoginScreen(@Body loginScreenBody: LoginScreenBody) :
        Response<LoginScreenResponse>
}
