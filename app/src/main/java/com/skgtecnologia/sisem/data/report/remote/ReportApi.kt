package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.report.remote.model.ReportBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {

    @POST("screen/addReport")
    suspend fun getAddReportRoleScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/addReportEntry")
    suspend fun getAddReportEntryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("novelty")
    suspend fun sendReport(@Body reportBody: ReportBody): Response<Unit>
}
