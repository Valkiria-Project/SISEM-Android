package com.skgtecnologia.sisem.core.data.remote.interceptors

import android.content.Context
import com.skgtecnologia.sisem.commons.resources.ANDROID_NETWORKING_FILE_NAME
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.core.data.remote.extensions.isUnauthorized
import com.skgtecnologia.sisem.core.data.remote.extensions.signWithToken
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

    private var retryCount = 0

    @Suppress("ComplexMethod")
    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.isUnauthorized() && retryCount < MAX_ATTEMPTS) {
            retryCount++

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
