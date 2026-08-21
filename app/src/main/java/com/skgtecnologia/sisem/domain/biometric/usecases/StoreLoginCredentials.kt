package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import javax.inject.Inject

/**
 * Encrypts and stores the crew member's login password on every successful login, refreshing
 * the inactivity timestamp so a constant user is never purged. The password is the only reusable
 * secret needed to silently re-authenticate after a biometric match.
 */
class StoreLoginCredentials @Inject constructor(
    private val biometricRepository: BiometricRepository
) {
    suspend operator fun invoke(username: String, password: String) =
        biometricRepository.storeLoginCredentials(username, password)
}
