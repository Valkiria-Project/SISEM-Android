package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.data.remote.extensions.isUnauthorized
import com.skgtecnologia.sisem.data.remote.extensions.signWithToken
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_ATTEMPTS = 3

@Singleton
class AccessTokenAuthenticator @Inject constructor(
    private val authRepository: AuthRepository
) : Authenticator {

    private var retryCount = 0

    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.isUnauthorized() && retryCount < MAX_ATTEMPTS) {
            retryCount++
            val currentToken = runBlocking {
                authRepository.observeCurrentAccessToken().first()
            }

            currentToken?.accessToken?.let {
                response.createSignedRequest(currentToken)
            }
        } else {
            retryCount = 0
            null
        }
    }

    private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request =
        synchronized(this) {
            val newToken = runBlocking {
                runCatching {
                    authRepository.refreshToken(
                        currentToken = currentToken
                    )
                }.getOrNull()
            }

            request.signWithToken(newToken?.accessToken.orEmpty())
        }
}
