package com.skgtecnologia.sisem.domain.auth.model

/**
 * Why a token refresh failed. The two cases call for opposite reactions, and telling them apart
 * is the whole point: an ambulance crossing a dead zone must not be signed out for it.
 */
sealed class SessionRefreshException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Keycloak answered and refused the refresh token. The session is over and the user has to
     * sign in again.
     */
    class Rejected(val code: Int) : SessionRefreshException("Refresh rejected with HTTP $code")

    /**
     * No usable answer came back: no signal, a timeout, Keycloak failing on its side. This says
     * nothing about the session itself, so the token is kept and the refresh is tried again on a
     * later request.
     */
    class Unreachable(
        detail: String,
        cause: Throwable? = null
    ) : SessionRefreshException("Refresh could not reach the server: $detail", cause)
}
