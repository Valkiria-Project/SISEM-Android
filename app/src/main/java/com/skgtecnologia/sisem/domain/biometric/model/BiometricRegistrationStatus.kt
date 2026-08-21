package com.skgtecnologia.sisem.domain.biometric.model

/**
 * Outcome of asking the cloud (GET v1/biometric/exists) whether a document already has
 * biometrics registered. The enrollment screen maps each case to a different UI:
 *
 * - [Registered]/[NotRegistered] come from a 200 and drive the "update" vs "register" CTA.
 * - [NetworkIntermittency] (404 / connectivity failure) and [QueryError] (5xx / unexpected)
 *   surface distinct banners so the líder can tell a flaky network from a backend problem.
 */
sealed interface BiometricRegistrationStatus {
    data object Registered : BiometricRegistrationStatus
    data object NotRegistered : BiometricRegistrationStatus
    data object NetworkIntermittency : BiometricRegistrationStatus
    data object QueryError : BiometricRegistrationStatus
}
