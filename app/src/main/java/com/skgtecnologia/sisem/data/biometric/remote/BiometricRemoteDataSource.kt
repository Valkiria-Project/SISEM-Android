package com.skgtecnologia.sisem.data.biometric.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricDocumentRequest
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricRequest
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.domain.biometric.model.BiometricModel
import timber.log.Timber
import javax.inject.Inject

class BiometricRemoteDataSource @Inject constructor(
    private val biometricApi: BiometricApi,
    private val networkApi: NetworkApi
) {

    suspend fun upload(
        username: String,
        role: String,
        embeddings: List<FloatArray>
    ): Result<Unit> = networkApi.apiCall {
        biometricApi.uploadBiometric(
            BiometricRequest(
                username = username,
                role = role,
                embeddings = embeddings.map { BiometricSerializer.floatArrayToBase64(it) }
            )
        )
    }.mapResult { }

    /**
     * Uploads the líder-registered biometrics identified only by document number.
     * No token/username/role is sent.
     */
    suspend fun uploadByDocument(
        document: String,
        embeddings: List<FloatArray>
    ): Result<Unit> = networkApi.apiCall {
        biometricApi.uploadBiometricByDocument(
            BiometricDocumentRequest(
                documentNumber = document,
                embeddings = embeddings.map { BiometricSerializer.floatArrayToBase64(it) }
            )
        )
    }.mapResult { }

    /**
     * Returns the cloud-stored biometrics for [documentNumber], or null if the person has
     * never enrolled (404) or if the request fails (network error).
     */
    suspend fun fetch(documentNumber: String): BiometricModel? =
        networkApi.apiCall {
            biometricApi.fetchBiometric(documentNumber)
        }.mapResult { response ->
            BiometricModel(
                documentNumber = response.documentNumber,
                embeddings = response.embeddings.map { BiometricSerializer.base64ToFloatArray(it) }
            )
        }.onFailure {
            Timber.d("[BiometricRemote] fetch: no cloud record for $documentNumber (${it.message})")
        }.getOrNull()
}
