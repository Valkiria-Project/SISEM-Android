package com.skgtecnologia.sisem.data.remote.interceptors

import android.content.Context
import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.commons.resources.ANDROID_NETWORKING_FILE_NAME
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.data.remote.extensions.isUnauthorized
import com.skgtecnologia.sisem.data.remote.extensions.signWithToken
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.valkiria.uicomponents.utlis.TimeUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_ATTEMPTS = 3

@Singleton
class AccessTokenAuthenticator @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageProvider: StorageProvider
) : Authenticator {

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    @Suppress("ComplexMethod")
    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.isUnauthorized() && responseCount(response) <= MAX_ATTEMPTS) {
            val url = response.request.url

            val token = runBlocking {
                when {
                    url.toString().contains(ASSISTANT_PRE_OP) ||
                        url.toString().contains(ASSISTANT_FINDING) ||
                        url.toString().contains(ASSISTANT_NOVELTY) ->
                        authRepository.getTokenByRole(
                            OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase()
                        )

                    url.toString().contains(DOCTOR_PRE_OP) ||
                        url.toString().contains(DOCTOR_FINDING) ||
                        url.toString().contains(DOCTOR_NOVELTY) ->
                        authRepository.getTokenByRole(OperationRole.MEDIC_APH.name.lowercase())

                    url.toString().contains(DRIVER_PRE_OP) ||
                        url.toString().contains(DRIVER_FINDING) ||
                        url.toString().contains(DRIVER_NOVELTY) ->
                        authRepository.getTokenByRole(OperationRole.DRIVER.name.lowercase())

                    url.toString().contains(APH) -> {
                        val token = authRepository.getTokenByRole(
                            OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase()
                        ) ?: authRepository.getTokenByRole(
                            OperationRole.MEDIC_APH.name.lowercase()
                        )

                        token
                    }

                    url.toString().contains(LOCATION) -> {
                        val token = authRepository.getTokenByRole(
                            OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase()
                        ) ?: authRepository.getTokenByRole(
                            OperationRole.MEDIC_APH.name.lowercase()
                        )

                        token
                    }

                    else -> authRepository.observeCurrentAccessToken().first()
                }
            }

            val authenticateContent = TimeUtils.getLocalDateTime(Instant.now()).toString() +
                "\t Authenticate intent: " + url +
                "\t with Token model: " + token +
                "\t using the refresh token: " + token?.refreshToken +
                "\t refreshed on: " + token?.refreshDateTime +
                "\n\n"

            storageProvider.storeContent(
                ANDROID_NETWORKING_FILE_NAME,
                Context.MODE_APPEND,
                authenticateContent.toByteArray()
            )

            token?.refreshToken?.let {
                response.createSignedRequest(token)
            }
        } else {
            null
        }
    }

    private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request? =
        synchronized(this) {
            val newToken = runBlocking {
                runCatching {
                    authRepository.refreshToken(
                        currentToken = currentToken
                    )
                }.getOrNull()
            }

            if (newToken == null) {
                runBlocking {
                    authRepository.deleteAccessTokenByUsername(currentToken.username)
                }
                UnauthorizedEventHandler.publishUnauthorizedEvent(currentToken.username)
                return@synchronized null
            }

            request.signWithToken(newToken.accessToken)
        }
}
