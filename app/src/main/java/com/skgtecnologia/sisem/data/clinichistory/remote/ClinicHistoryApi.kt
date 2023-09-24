package com.skgtecnologia.sisem.data.clinichistory.remote

import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ClinicHistoryApi {

    @POST("screen/aph")
    suspend fun getClinicHistoryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
