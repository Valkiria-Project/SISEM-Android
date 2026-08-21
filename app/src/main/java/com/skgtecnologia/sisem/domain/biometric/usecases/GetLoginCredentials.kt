package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import com.skgtecnologia.sisem.domain.biometric.model.LoginCredentials
import javax.inject.Inject

/**
 * Returns the decrypted credentials for [username], or null when there are none stored (or they
 * cannot be decrypted). Used to run the normal login after a biometric match.
 */
class GetLoginCredentials @Inject constructor(
    private val biometricRepository: BiometricRepository
) {
    suspend operator fun invoke(username: String): LoginCredentials? =
        biometricRepository.getLoginCredentials(username)
}
