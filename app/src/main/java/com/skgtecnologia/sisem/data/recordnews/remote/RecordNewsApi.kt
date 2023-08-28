package com.skgtecnologia.sisem.data.recordnews.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecordNewsApi {

    @POST("screen/addReportEntry")
    suspend fun getRecordNewsScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
