package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ReportApi {

    @POST("screen/addReport")
    suspend fun getAddReportRoleScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/addReportEntry")
    suspend fun getAddReportEntryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @Multipart
    @POST("novelty/{idTurn}")
    suspend fun sendReport(
        @Path("idTurn") idTurn: String,
        @PartMap partMap: Map<String, RequestBody>,
        @PartMap files: Map<String, RequestBody>
    ): Response<Unit>
}
