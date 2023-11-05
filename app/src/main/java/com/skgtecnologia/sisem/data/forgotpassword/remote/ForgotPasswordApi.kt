package com.skgtecnologia.sisem.data.forgotpassword.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ForgotPasswordApi {

    @POST("screen/forgot-password")
    suspend fun getForgotPasswordScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @GET("password/send-email")
    suspend fun sendEmail(@Header("email") email: String): Response<Unit>
}
