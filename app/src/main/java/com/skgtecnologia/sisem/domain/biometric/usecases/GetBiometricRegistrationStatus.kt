package com.skgtecnologia.sisem.domain.biometric.usecases

import com.skgtecnologia.sisem.domain.biometric.BiometricRepository
import com.skgtecnologia.sisem.domain.biometric.model.BiometricRegistrationStatus
import javax.inject.Inject

class GetBiometricRegistrationStatus @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    /**
     * Asks the cloud (GET v1/biometric/exists) whether [documentNumber] already has
     * biometrics registered. Drives the enrollment CTA between "register" and "update",
     * and surfaces network/backend failures as distinct states.
     */
    suspend operator fun invoke(documentNumber: String): BiometricRegistrationStatus =
        biometricRepository.registrationStatus(documentNumber)
}
