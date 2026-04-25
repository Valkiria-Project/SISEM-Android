package com.skgtecnologia.sisem.data.signature.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.signature.remote.model.SignatureBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SignatureApi {

    @POST("screen/crew-signature-step1")
    suspend fun getInitSignatureScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/crew-signature-step2")
    suspend fun getSignatureScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @GET("crew/{document}")
    suspend fun searchDocument(@Path("document") document: String): Response<Unit>

    @POST("crew")
    suspend fun registerSignature(@Body signatureBody: SignatureBody): Response<Unit>
}
