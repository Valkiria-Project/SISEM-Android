package com.skgtecnologia.sisem.data.biometric.remote

import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricDocumentRequest
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricExistsResponse
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Backend contract for cloud biometric storage.
 *
 * POST v1/biometric — upload embeddings after enrollment.
 * GET  v1/biometric/exists — existence check by document number (no embeddings returned).
 */
interface BiometricApi {

    @GET("biometric/exists")
    suspend fun biometricExists(
        @Header("documentNumber") documentNumber: String
    ): Response<BiometricExistsResponse>

    @POST("biometric")
    suspend fun uploadBiometric(
        @Body request: BiometricRequest
    ): Response<Unit>

    @POST("biometric")
    suspend fun uploadBiometricByDocument(
        @Body request: BiometricDocumentRequest
    ): Response<Unit>
}
