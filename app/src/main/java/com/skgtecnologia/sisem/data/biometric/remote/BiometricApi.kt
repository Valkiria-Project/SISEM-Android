package com.skgtecnologia.sisem.data.biometric.remote

import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricDocumentRequest
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricRequest
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Backend contract for cloud biometric storage.
 *
 * POST v1/biometric — upload embeddings after enrollment.
 * GET  v1/biometric — fetch embeddings by document number on login (404 = not enrolled).
 */
interface BiometricApi {

    @GET("v1/biometric")
    suspend fun fetchBiometric(
        @Header("documentNumber") documentNumber: String
    ): Response<BiometricResponse>

    @POST("v1/biometric")
    suspend fun uploadBiometric(
        @Body request: BiometricRequest
    ): Response<Unit>

    @POST("v1/biometric")
    suspend fun uploadBiometricByDocument(
        @Body request: BiometricDocumentRequest
    ): Response<Unit>
}
