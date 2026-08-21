package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import timber.log.Timber
import javax.inject.Inject

class StoreBiometricFromLogin @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    /**
     * Persists the biometrics that the auth/login response already carries. Embeddings are
     * base64-encoded and stored locally keyed by [username] (with [role] and
     * [documentNumber]) so the biometric login button can appear on the next visit.
     * A login without enrolled biometrics returns an empty list and is a no-op.
     */
    suspend operator fun invoke(
        username: String,
        role: String,
        documentNumber: String,
        embeddings: List<String>
    ) {
        if (embeddings.isEmpty()) {
            Timber.d("[Biometric] Login for $username carried no embeddings")
            return
        }
        Timber.d("[Biometric] Persisting ${embeddings.size} login embeddings for $username")
        biometricRepository.storeFromLogin(username, role, documentNumber, embeddings)
    }
}
