package com.skgtecnologia.sisem.domain.auth.usecases

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import javax.inject.Inject

/**
 * Detects when a matched biometric user already has an active local session under a role
 * other than [targetRole] — e.g. logged in as Conductor, now trying to enter via the
 * Auxiliar card, or during a shift change trying to re-enter a different role than the one
 * that was vacated. Such a match must not silently switch sessions.
 */
class GetConflictingActiveSession @Inject constructor(
    private val getAllAccessTokens: GetAllAccessTokens
) {

    suspend operator fun invoke(username: String, targetRole: String): AccessTokenModel? {
        if (targetRole.isBlank()) return null

        return getAllAccessTokens().getOrNull()
            ?.firstOrNull { it.username.equals(username, ignoreCase = true) }
            ?.takeIf { !it.role.equals(targetRole, ignoreCase = true) }
    }
}
