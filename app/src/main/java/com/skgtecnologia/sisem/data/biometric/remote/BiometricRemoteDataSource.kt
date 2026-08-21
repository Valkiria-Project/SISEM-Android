package com.skgtecnologia.sisem.data.biometric.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricDocumentRequest
import com.skgtecnologia.sisem.data.biometric.remote.model.BiometricRequest
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_NOT_FOUND_STATUS_CODE
import com.skgtecnologia.sisem.domain.biometric.model.BiometricRegistrationStatus
import timber.log.Timber
import java.io.IOException
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
     * Existence check that maps HTTP outcomes to a domain status. It bypasses
     * [NetworkApi.apiCall] on purpose: that wrapper collapses every failure into a generic
     * banner, but here the caller needs to tell a 404 (treated as network intermittency)
     * apart from a 5xx/unexpected error.
     */
    @Suppress("TooGenericExceptionCaught")
    suspend fun exists(documentNumber: String): BiometricRegistrationStatus = try {
        val response = biometricApi.biometricExists(documentNumber)
        val body = response.body()

        when {
            response.isSuccessful && body != null -> {
                if (body.registered) {
                    BiometricRegistrationStatus.Registered
                } else {
                    BiometricRegistrationStatus.NotRegistered
                }
            }

            response.code() == HTTP_NOT_FOUND_STATUS_CODE -> {
                BiometricRegistrationStatus.NetworkIntermittency
            }

            else -> {
                BiometricRegistrationStatus.QueryError
            }
        }
    } catch (exception: IOException) {
        Timber.d("[BiometricRemote] exists: network failure for $documentNumber (${exception.message})")
        BiometricRegistrationStatus.NetworkIntermittency
    } catch (exception: Exception) {
        Timber.wtf(exception, "[BiometricRemote] exists: unexpected failure for $documentNumber")
        BiometricRegistrationStatus.QueryError
    }
}
