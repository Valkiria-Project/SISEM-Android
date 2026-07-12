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
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
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

    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.isUnauthorized() && responseCount(response) <= MAX_ATTEMPTS) {
            val url = response.request.url

            val token = runBlocking { resolveTokenFor(url.toString()) }

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

    @Suppress("ComplexMethod")
    private suspend fun resolveTokenFor(url: String): AccessTokenModel? = when {
        url.contains(ASSISTANT_PRE_OP) ||
            url.contains(ASSISTANT_FINDING) ||
            url.contains(ASSISTANT_NOVELTY) -> {
            authRepository.getTokenByRole(OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase())
        }

        url.contains(DOCTOR_PRE_OP) ||
            url.contains(DOCTOR_FINDING) ||
            url.contains(DOCTOR_NOVELTY) -> {
            authRepository.getTokenByRole(OperationRole.MEDIC_APH.name.lowercase())
        }

        url.contains(DRIVER_PRE_OP) ||
            url.contains(DRIVER_FINDING) ||
            url.contains(DRIVER_NOVELTY) -> {
            authRepository.getTokenByRole(OperationRole.DRIVER.name.lowercase())
        }

        url.contains(APH) || url.contains(LOCATION) -> {
            authRepository.getTokenByRole(OperationRole.AUXILIARY_AND_OR_TAPH.name.lowercase())
                ?: authRepository.getTokenByRole(OperationRole.MEDIC_APH.name.lowercase())
        }

        else -> {
            authRepository.observeCurrentAccessToken().first()
        }
    }

    private fun Response.createSignedRequest(currentToken: AccessTokenModel): Request? =
        synchronized(this) {
            val result = runBlocking {
                runCatching {
                    authRepository.refreshToken(
                        currentToken = currentToken
                    )
                }
            }
            val newToken = result.getOrNull()

            if (newToken == null) {
                val throwable = result.exceptionOrNull()
                val errorMessage = when {
                    throwable is BannerModel -> {
                        "${throwable.title}: ${throwable.description}"
                    }

                    throwable != null -> {
                        throwable.message ?: throwable::class.simpleName
                        ?: "UnknownError"
                    }

                    else -> {
                        "UnknownError"
                    }
                }

                storageProvider.storeContent(
                    ANDROID_NETWORKING_FILE_NAME,
                    Context.MODE_APPEND,
                    (
                        TimeUtils.getLocalDateTime(Instant.now()).toString() +
                        "\t Refreshed Token failure: " + errorMessage +
                        "\t using the refresh token: " + currentToken.refreshToken +
                        "\n\n"
                    ).toByteArray()
                )

                // Drop the dead token from the cache. If we keep it, the next
                // 401-driven authenticate() call reads the same expired refresh
                // token, fails again, and emits another UnauthorizedSession event
                // — looping the user back to the login screen on every retry.
                runBlocking {
                    authRepository.deleteAccessTokenByUsername(currentToken.username)
                }
                UnauthorizedEventHandler.publishUnauthorizedEvent(currentToken.username)
                return@synchronized null
            }

            request.signWithToken(newToken.accessToken)
        }
}
