package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.core.data.remote.model.screen.ScreenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ReportApi {

    @POST("screen/addReport")
    suspend fun getAddReportRoleScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/addReportEntry")
    suspend fun getAddReportEntryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @Multipart
    @POST("novelty/assistant/{idTurn}")
    suspend fun sendAuxReport(
        @Path("idTurn") idTurn: String,
        @Part("subject") topic: RequestBody,
        @Part("description") description: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Unit>

    @Multipart
    @POST("novelty/doctor/{idTurn}")
    suspend fun sendDoctorReport(
        @Path("idTurn") idTurn: String,
        @Part("subject") topic: RequestBody,
        @Part("description") description: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Unit>

    @Multipart
    @POST("novelty/driver/{idTurn}")
    suspend fun sendDriverReport(
        @Path("idTurn") idTurn: String,
        @Part("subject") topic: RequestBody,
        @Part("description") description: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Unit>
}
