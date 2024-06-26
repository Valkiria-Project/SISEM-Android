package com.skgtecnologia.sisem.data.remote.interceptors

import com.skgtecnologia.sisem.data.remote.extensions.signWithToken
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

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

    @Suppress("ComplexMethod")
    private fun Request.signedRequest(): Request? = runBlocking {
        when {
            url.toString().contains(ASSISTANT_PRE_OP) ||
                url.toString().contains(ASSISTANT_FINDING) ||
                url.toString().contains(ASSISTANT_NOVELTY) ->
                authRepository.getTokenByRole(OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase())
                    ?.let { token ->
                        signWithToken(token.accessToken)
                    }

            url.toString().contains(DOCTOR_PRE_OP) ||
                url.toString().contains(DOCTOR_FINDING) ||
                url.toString().contains(DOCTOR_NOVELTY) ->
                authRepository.getTokenByRole(OperationRole.MEDIC_APH.name.lowercase())
                    ?.let { token ->
                        signWithToken(token.accessToken)
                    }

            url.toString().contains(DRIVER_PRE_OP) ||
                url.toString().contains(DRIVER_FINDING) ||
                url.toString().contains(DRIVER_NOVELTY) ->
                authRepository.getTokenByRole(OperationRole.DRIVER.name.lowercase())
                    ?.let { token ->
                        signWithToken(token.accessToken)
                    }

            url.toString().contains(APH) -> {
                val token = authRepository.getTokenByRole(
                    OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase()
                ) ?: authRepository.getTokenByRole(
                    OperationRole.MEDIC_APH.name.lowercase()
                )

                token?.let { signWithToken(token.accessToken) }
            }

            url.toString().contains(LOCATION) -> {
                val token = authRepository.getTokenByRole(
                    OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase()
                ) ?: authRepository.getTokenByRole(
                    OperationRole.MEDIC_APH.name.lowercase()
                )

                token?.let { signWithToken(token.accessToken) }
            }

            else -> authRepository.getLastToken()?.let { accessToken ->
                signWithToken(accessToken)
            }
        }
    }
}
