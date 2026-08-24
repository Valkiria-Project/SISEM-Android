package com.skgtecnologia.sisem.domain.biometric.model

/**
 * Outcome of asking the cloud (GET v1/biometric/exists) whether a document already has
 * biometrics registered. The enrollment screen maps each case to a different UI:
 *
 * - [Registered] carries the crew member's identity data returned by the backend and drives
 *   the "update" CTA; [NotRegistered] (200 with `exists: false`, or a 404 — both mean the
 *   document has no biometric record yet) drives the "register" CTA.
 * - [NetworkIntermittency] (true connectivity failure) and [QueryError] (5xx / unexpected)
 *   surface distinct banners so the líder can tell a flaky network from a backend problem.
 */
sealed interface BiometricRegistrationStatus {
    data class Registered(
        val userName: String,
        val userLastName: String,
        val documentNumber: String,
        val role: String
    ) : BiometricRegistrationStatus

    data object NotRegistered : BiometricRegistrationStatus
    data object NetworkIntermittency : BiometricRegistrationStatus
    data object QueryError : BiometricRegistrationStatus
}
