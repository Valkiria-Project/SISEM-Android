package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.data.medicalhistory.remote.model.DeleteAphFileBody
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.MedicalHistoryBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
        @Part("id_aph") idAph: RequestBody,
        @Part files: List<MultipartBody.Part>,
        @Part("novelty") description: RequestBody?
    ): Response<Unit>

    @POST("screen/aph-view")
    suspend fun getMedicalHistoryViewScreen(@Body screenBody: ScreenBody): Response<ScreenResponse>

    @POST("aph/delete-file")
    suspend fun deleteAphFile(@Body deleteAphFileBody: DeleteAphFileBody): Response<Unit>
}
