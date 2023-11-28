package com.skgtecnologia.sisem.data.stretcherretention.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import com.skgtecnologia.sisem.data.stretcherretention.remote.model.StretcherRetentionBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StretcherRetentionApi {

    @POST("screen/aph-stretcher-retention")
    suspend fun getStretcherRetentionScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("aph/save-stretcher-retention")
    suspend fun saveStretcherRetention(
        @Body stretcherRetentionBody: StretcherRetentionBody
    ): Response<Unit>

    @POST("screen/aph-stretcher-retention-view")
    suspend fun getStretcherRetentionViewScreen(
        @Body screenBody: ScreenBody
    ): Response<ScreenResponse>
}
