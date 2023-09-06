package com.skgtecnologia.sisem.data.report.remote

import com.skgtecnologia.sisem.data.report.remote.model.ReportBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {

    @POST("novelty")
    suspend fun sendReport(@Body reportBody: ReportBody): Response<Unit>
}
