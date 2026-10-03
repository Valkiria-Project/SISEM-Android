package com.skgtecnologia.sisem.data.offline.outbox

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.SessionRefreshException
import okhttp3.Request
import java.time.LocalDateTime
import javax.inject.Inject

sealed interface SigningResult {

    data class Signed(val request: Request) : SigningResult

    /** Whoever made the write has no usable session on this device right now. */
    data class WaitForCreator(val reason: String) : SigningResult

    /** Nobody was signed in when it was made, so the server would refuse it whoever sent it. */
    data object NoCreator : SigningResult
}

/**
 * Signs a resent write as the user who made it — and only as them.
 *
 * The live interceptor picks a token by role. For a write sent late that is wrong: if an
 * auxiliary records a medical history with no signal and hands over the shift before it goes out,
 * the role's token by then belongs to the next auxiliary, and the history would reach the server
 * under someone who never saw the patient. So a resend waits for its own creator instead.
 */
class OutboxSigner(
    private val authRepository: AuthRepository,
    private val now: () -> LocalDateTime
) {

    @Inject
    constructor(authRepository: AuthRepository) : this(authRepository, LocalDateTime::now)

    suspend fun sign(request: Request, createdBy: String?): SigningResult {
        val token = createdBy?.let { username ->
            authRepository.getAllAccessTokens().firstOrNull { it.username == username }
        }

        return when {
            createdBy == null -> SigningResult.NoCreator
            token == null -> SigningResult.WaitForCreator("$createdBy has no session on this device")
            now() > token.expDate -> refreshAndSign(request, token)
            else -> request.signedWith(token)
        }
    }

    private suspend fun refreshAndSign(request: Request, token: AccessTokenModel): SigningResult =
        resultOf { authRepository.refreshToken(token) }.fold(
            onSuccess = { request.signedWith(it) },
            onFailure = { error ->
                SigningResult.WaitForCreator(
                    if (error is SessionRefreshException.Rejected) {
                        "the session of ${token.username} has ended"
                    } else {
                        "could not refresh the session of ${token.username}: ${error.message}"
                    }
                )
            }
        )

    private fun Request.signedWith(token: AccessTokenModel): SigningResult = SigningResult.Signed(
        newBuilder().header(AUTHORIZATION_HEADER, token.accessToken.asBearer()).build()
    )
}
