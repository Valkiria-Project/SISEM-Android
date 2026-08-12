package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import timber.log.Timber
import javax.inject.Inject

class FetchBiometric @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    /**
     * Checks the cloud for biometrics registered for [username].
     *
     * Returns `true` if cloud data was found and stored locally — the caller should
     * skip the enrollment prompt.
     * Returns `false` if the user has never enrolled (404) or if a network error
     * occurred — the caller should show the enrollment prompt.
     */
    suspend operator fun invoke(username: String): Boolean {
        val remote = biometricRepository.fetchFromCloud(username) ?: run {
            Timber.d("[Biometric] No cloud record for $username")
            return false
        }
        Timber.d("[Biometric] Cloud record found for $username — persisting locally")
        biometricRepository.storeFromCloud(
            username = remote.username,
            role = remote.role,
            embeddings = remote.embeddings
        )
        return true
    }
}
