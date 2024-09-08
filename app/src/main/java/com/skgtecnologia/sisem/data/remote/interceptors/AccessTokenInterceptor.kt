package com.skgtecnologia.sisem.data.remote.interceptors

import android.content.Context
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.resources.ANDROID_NETWORKING_FILE_NAME
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.data.remote.extensions.signWithToken
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.valkiria.uicomponents.utlis.TimeUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.net.ConnectException
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenInterceptor @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageProvider: StorageProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = try {
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

    @Suppress("ComplexMethod")
    private fun Request.signedRequest(): Request? = runBlocking {
        handleTokenExpirationTime()

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

    private suspend fun Request.handleTokenExpirationTime() {
        authRepository.getAllAccessTokens().map { accessTokenModel ->
            if (LocalDateTime.now() > accessTokenModel.expDate) {
                val authenticateContent = TimeUtils.getLocalDateTime(Instant.now()).toString() +
                    "\t Authenticate intent: " + url +
                    "\t with Token model: " + accessTokenModel +
                    "\t using the refresh token: " + accessTokenModel.refreshToken +
                    "\t refreshed on: " + accessTokenModel.refreshDateTime +
                    "\n\n"

                storageProvider.storeContent(
                    ANDROID_NETWORKING_FILE_NAME,
                    Context.MODE_APPEND,
                    authenticateContent.toByteArray()
                )

                resultOf { authRepository.refreshToken(accessTokenModel) }
                    .onSuccess { refreshedTokenModel ->
                        val refreshSuccessfulContent =
                            TimeUtils.getLocalDateTime(Instant.now()).toString() +
                                "\t Refreshed Token model: " + refreshedTokenModel +
                                "\t using the refresh token: " + accessTokenModel.refreshToken +
                                "\t refreshed on: " + refreshedTokenModel.refreshDateTime +
                                "\n\n"

                        storageProvider.storeContent(
                            ANDROID_NETWORKING_FILE_NAME,
                            Context.MODE_APPEND,
                            refreshSuccessfulContent.toByteArray()
                        )
                    }
                    .onFailure { throwable ->
                        val refreshFailureContent =
                            TimeUtils.getLocalDateTime(Instant.now()).toString() +
                                "\t Refreshed Token failure: " + throwable.localizedMessage +
                                "\t using the refresh token: " + accessTokenModel.refreshToken +
                                "\n\n"

                        storageProvider.storeContent(
                            ANDROID_NETWORKING_FILE_NAME,
                            Context.MODE_APPEND,
                            refreshFailureContent.toByteArray()
                        )
                    }
            }
        }
    }
}
