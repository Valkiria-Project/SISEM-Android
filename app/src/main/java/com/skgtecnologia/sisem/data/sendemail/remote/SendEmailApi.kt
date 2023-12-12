package com.skgtecnologia.sisem.data.sendemail.remote

import com.skgtecnologia.sisem.data.sendemail.remote.model.SendEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SendEmailApi {

    @POST("aph/send-mail")
    suspend fun sendEmail(@Body sendEmailBody: SendEmailBody): Response<Unit>
}
