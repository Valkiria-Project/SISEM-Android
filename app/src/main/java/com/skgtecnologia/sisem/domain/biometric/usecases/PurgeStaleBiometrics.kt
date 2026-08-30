package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import javax.inject.Inject

private const val MAX_IDLE_DAYS = 10

/**
 * Deletes biometric records (and their encrypted credentials) that have been idle for more than
 * [MAX_IDLE_DAYS] calendar days. Evaluated every time the login screen loads.
 */
class PurgeStaleBiometrics @Inject constructor(
    private val biometricRepository: BiometricRepository
) {
    suspend operator fun invoke() = biometricRepository.purgeStale(MAX_IDLE_DAYS)
}
