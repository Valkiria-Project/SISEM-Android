package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import timber.log.Timber
import javax.inject.Inject

class FetchBiometric @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    /**
     * Checks the cloud for biometrics registered under [documentNumber] and, when found,
     * persists them locally keyed by [username] (with [role] and [documentNumber]) so the
     * biometric login button can appear. Username and role come from the caller's session
     * because the cloud record is keyed only by document.
     *
     * Returns `true` if cloud data was found and stored locally, `false` on 404/network error.
     */
    suspend operator fun invoke(username: String, role: String, documentNumber: String): Boolean {
        val remote = biometricRepository.fetchFromCloud(documentNumber) ?: run {
            Timber.d("[Biometric] No cloud record for document $documentNumber")
            return false
        }
        Timber.d("[Biometric] Cloud record found for $username (doc $documentNumber) — persisting locally")
        biometricRepository.storeFromCloud(
            username = username,
            role = role,
            documentNumber = remote.documentNumber,
            embeddings = remote.embeddings
        )
        return true
    }
}
