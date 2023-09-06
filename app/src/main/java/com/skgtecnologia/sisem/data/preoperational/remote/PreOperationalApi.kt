package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.preoperational.remote.model.SavePreOperationalBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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
}
