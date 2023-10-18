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

interface ReportApi {

    @POST("screen/addReport")
    suspend fun getAddReportRoleScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/addReportEntry")
    suspend fun getAddReportEntryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @Multipart
    @POST("novelty")
    suspend fun sendReport(
        @PartMap partMap: Map<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): Response<Unit>
}
