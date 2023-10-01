package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.data.remote.extensions.isRequestWithAccessToken
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

@Singleton
class AccessTokenAuthenticator @Inject constructor(
    private val authRepository: AuthRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.isUnauthorized()) {
            val currentToken = runBlocking {
                authRepository.observeCurrentAccessToken().first()
            }

            if (!isRequestWithAccessToken(response) || currentToken?.accessToken == null) {
                null
            } else {
                response.createSignedRequest(currentToken)
            }
        } else {
            null
        }
    }

    private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request? =
        synchronized(this) {
            val newToken = runBlocking {
                authRepository.refreshToken(refreshToken = currentToken.refreshToken)
            }

            if (currentToken.accessToken != newToken.accessToken) {
                request.signWithToken(newToken.accessToken)
            } else {
                null
            }
        }
}
