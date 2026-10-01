package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.mapToDomain
import com.skgtecnologia.sisem.data.remote.api.NetworkApi
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_BAD_REQUEST_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_UNAUTHORIZED_STATUS_CODE
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.skgtecnologia.sisem.domain.auth.model.RefreshTokenModel
import com.skgtecnologia.sisem.domain.auth.model.SessionRefreshException
import javax.inject.Inject

// What Keycloak's token endpoint answers when it looked at the refresh token and refused it:
// 400 invalid_grant for an expired, revoked or ended session, 401 for a client it does not
// accept. Anything else is a failure to get an answer, not an answer.
private val REJECTED_STATUS_CODES = setOf(
    HTTP_BAD_REQUEST_STATUS_CODE,
    HTTP_UNAUTHORIZED_STATUS_CODE
)

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val networkApi: NetworkApi
) {

    suspend fun authenticate(
        username: String,
        password: String,
        code: String,
        turnId: String,
        forceCloseSession: Boolean = false
    ): Result<AccessTokenModel> = networkApi.apiCall {
        authApi.authenticate(
            authenticateBody = AuthenticateBody(
                username = username,
                password = password,
                code = code,
                idTurn = turnId,
                forceCloseSession = forceCloseSession
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    /**
     * Goes around [NetworkApi.apiCall] on purpose. That wrapper turns every failure into a banner
     * for the UI, which throws away the one thing the callers of this need to know: whether
     * Keycloak refused the token or simply could not be reached. Only the first should end the
     * session — see [SessionRefreshException].
     */
    suspend fun refreshToken(refreshToken: String): Result<RefreshTokenModel> {
        val response = resultOf { authApi.refresh(refreshToken = refreshToken) }
            .getOrElse { error ->
                return Result.failure(
                    SessionRefreshException.Unreachable(
                        detail = error::class.simpleName.orEmpty(),
                        cause = error
                    )
                )
            }

        val body = response.body()

        return when {
            response.isSuccessful && body != null -> Result.success(body.mapToDomain())

            response.code() in REJECTED_STATUS_CODES -> Result.failure(
                SessionRefreshException.Rejected(code = response.code())
            )

            else -> Result.failure(
                SessionRefreshException.Unreachable(detail = "HTTP ${response.code()}")
            )
        }
    }

    suspend fun logout(username: String, refreshToken: String): Result<String> =
        networkApi.apiCall {
            authApi.logout(username = username, refreshToken = refreshToken)
        }.mapResult {
            username
        }
}
