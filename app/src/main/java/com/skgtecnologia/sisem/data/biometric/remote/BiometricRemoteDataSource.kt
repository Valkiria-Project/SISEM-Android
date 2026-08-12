package com.skgtecnologia.sisem.data.biometric.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
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
     * Returns the cloud-stored biometrics, or null if the user has never enrolled
     * (404) or if the request fails (network error — caller shows enrollment prompt).
     */
    suspend fun fetch(username: String): BiometricModel? =
        networkApi.apiCall {
            biometricApi.fetchBiometric(username)
        }.mapResult { response ->
            BiometricModel(
                username = response.username,
                role = response.role,
                embeddings = response.embeddings.map { BiometricSerializer.base64ToFloatArray(it) }
            )
        }.onFailure {
            Timber.d("[BiometricRemote] fetch: no cloud record for $username (${it.message})")
        }.getOrNull()
}
