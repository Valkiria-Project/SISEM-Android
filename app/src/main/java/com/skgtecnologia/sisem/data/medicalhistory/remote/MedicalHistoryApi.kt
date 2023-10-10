package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.data.medicalhistory.remote.model.MedicalHistoryBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MedicalHistoryApi {

    @POST("screen/aph")
    suspend fun getMedicalHistoryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("aph")
    suspend fun sendMedicalHistory(@Body medicalHistoryBody: MedicalHistoryBody): Response<Unit>
}
