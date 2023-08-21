package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.data.remote.extensions.signWithToken
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

@Singleton
class AccessTokenInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val newRequest = chain.request().signedRequest()

            if (newRequest != null) {
                chain.proceed(newRequest)
            } else {
                chain.proceed(chain.request())
            }
        } catch (connectException: ConnectException) {
            Timber.d("ConnectException ${connectException.message}")
            chain.proceed(chain.request())
        }
    }

    private fun Request.signedRequest(): Request? = runBlocking {
        authRepository.getLastToken()?.let { accessToken ->
            signWithToken(accessToken)
        }
    }
}
