package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.preoperational.remote.model.SavePreOperationalBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap

interface PreOperationalApi {

    @POST("screen/pre-operational-assistant")
    suspend fun getAuxPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("screen/pre-operational-doctor")
    suspend fun getDoctorPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("screen/pre-operational-driver")
    suspend fun getDriverPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("preoperational")
    suspend fun sendPreOperational(
        @Body savePreOperationalBody: SavePreOperationalBody
    ): Response<Unit>

    @Multipart
    @POST("preoperational/novelty")
    suspend fun sendFinding(
        @PartMap partMap: Map<String, RequestBody>,
        @PartMap images: Map<String, RequestBody>
    ): Response<Unit>
}
