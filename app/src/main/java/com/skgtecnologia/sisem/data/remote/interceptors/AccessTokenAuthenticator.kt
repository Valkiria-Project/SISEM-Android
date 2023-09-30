package com.skgtecnologia.sisem.data.remote.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenAuthenticator @Inject constructor(
    private val comparenderaDataStorage: ComparenderaDataStorage,
    private val oAuthApi: OAuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = try {
        response.createSignedRequest()
    } catch (e: IOException) {
        null
    }

    private fun Response.createSignedRequest(): Request? = synchronized(this) {
        comparenderaDataStorage.getRefreshToken()?.let { refreshToken ->
            val response = runBlocking {
                oAuthApi.refreshUserAccessToken(
                    refreshUserAccessTokenBody = RefreshUserAccessTokenBody(refreshToken = refreshToken)
                )
            }

            val body = response.body()

            if (response.isSuccessful && body != null) {
                request.signWithToken(body.accessToken)
            } else {
                null
            }
        } ?: run { null }
    }
}
