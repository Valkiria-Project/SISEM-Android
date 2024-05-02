package com.skgtecnologia.sisem.data.preoperational.remote

import com.skgtecnologia.sisem.data.preoperational.remote.model.SavePreOperationalBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

@Suppress("TooManyFunctions")
interface PreOperationalApi {

    @POST("screen/pre-operational-assistant")
    suspend fun getAuxPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("preoperational/pre-operational-assistant")
    suspend fun sendAuxPreOperational(
        @Body savePreOperationalBody: SavePreOperationalBody
    ): Response<Unit>

    @POST("screen/pre-operational-doctor")
    suspend fun getDoctorPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("preoperational/pre-operational-doctor")
    suspend fun sendDoctorPreOperational(
        @Body savePreOperationalBody: SavePreOperationalBody
    ): Response<Unit>

    @POST("screen/pre-operational-driver")
    suspend fun getDriverPreOperationalScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("preoperational/pre-operational-driver")
    suspend fun sendDriverPreOperational(
        @Body savePreOperationalBody: SavePreOperationalBody
    ): Response<Unit>

    @Multipart
    @POST("preoperational/novelty")
    suspend fun sendFinding(
        @Part("type") type: RequestBody,
        @Part("id_preoperational") idPreoperational: RequestBody,
        @Part("id_turn") idTurn: RequestBody,
        @Part("novelty") novelty: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<Unit>

    @POST("screen/crewListView")
    suspend fun getAuthCardViewScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("screen/pre-operational-assistant-view")
    suspend fun getAuxPreOperationalScreenView(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("screen/pre-operational-doctor-view")
    suspend fun getDoctorPreOperationalScreenView(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>

    @POST("screen/pre-operational-driver-view")
    suspend fun getDriverPreOperationalScreenView(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>
}
