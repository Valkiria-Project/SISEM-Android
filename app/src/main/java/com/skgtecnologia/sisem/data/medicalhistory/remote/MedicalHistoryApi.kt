package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.data.medicalhistory.remote.model.MedicalHistoryBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MedicalHistoryApi {

    @POST("screen/aph")
    suspend fun getMedicalHistoryScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/aph-vital-signs")
    suspend fun getVitalSignsScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("screen/aph-medicines")
    suspend fun getMedicineScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("aph")
    suspend fun sendMedicalHistory(@Body medicalHistoryBody: MedicalHistoryBody): Response<Unit>

    @Multipart
    @POST("aph/files")
    suspend fun saveAphFiles(
        @Path("id_aph") idAph: String,
        @Part files: List<MultipartBody.Part>
    ): Response<Unit>

    @POST("screen/aph-view")
    suspend fun getMedicalHistoryViewScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>
}
