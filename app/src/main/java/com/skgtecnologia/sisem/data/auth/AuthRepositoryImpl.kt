package com.skgtecnologia.sisem.data.auth

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.auth.remote.AuthRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.valkiria.uicomponents.utlis.TimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : AuthRepository {

    override suspend fun authenticate(username: String, password: String): AccessTokenModel {
        val code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty()
        val turn = authCacheDataSource.observeAccessToken().first()?.turn
        val turnId = turn?.id?.toString()

        return authRemoteDataSource.authenticate(
            username = username,
            password = password,
            code = code,
            turnId = turnId.orEmpty()
        ).mapResult { accessTokenModel ->
            if (accessTokenModel.isAdmin) {
                getAllAccessTokens().forEach { accessToken ->
                    logout(accessToken.username)
                }
            }

            if (turnId == null) {
                Firebase.messaging.subscribeToTopic(code).addOnCompleteListener { task ->
                    var msg = "Subscribed to $code"

                    if (!task.isSuccessful) {
                        msg = "Subscribe to $code failed"
                    }

                    Timber.d(msg)
                }
            }

            val returnedTurnId = accessTokenModel.turn?.id.toString()
            val accessToken = if (turnId == returnedTurnId && turn.isComplete) {
                accessTokenModel.copy(
                    preoperational = accessTokenModel.preoperational?.copy(
                        status = false
                    )
                )
            } else {
                accessTokenModel
            }
            authCacheDataSource.storeAccessToken(accessToken)
            accessToken
        }.getOrThrow()
    }

    override suspend fun refreshToken(currentToken: AccessTokenModel): AccessTokenModel =
        authRemoteDataSource.refreshToken(refreshToken = currentToken.refreshToken)
            .mapResult { refreshTokenModel ->
                val token = currentToken.copy(
                    accessToken = refreshTokenModel.accessToken,
                    refreshToken = refreshTokenModel.refreshToken,
                    refreshDateTime = TimeUtils.getLocalDateTime(Instant.now()),
                    expDate = refreshTokenModel.expDate
                )
                token
            }.onSuccess { accessTokenModel ->
                authCacheDataSource.storeAccessToken(accessTokenModel)
            }.getOrThrow()

    override suspend fun getLastToken(): String? =
        authCacheDataSource.observeAccessToken().first()?.accessToken

    override suspend fun observeCurrentAccessToken(): Flow<AccessTokenModel?> =
        authCacheDataSource.observeAccessToken()

    override suspend fun getAllAccessTokens(): List<AccessTokenModel> =
        authCacheDataSource.retrieveAllAccessTokens()

    override suspend fun getTokenByRole(role: String): AccessTokenModel? =
        authCacheDataSource.retrieveAccessTokenByRole(role)

    override suspend fun logout(username: String): String =
        authRemoteDataSource.logout(username)
            .onSuccess {
                authCacheDataSource.deleteAccessTokenByUsername(username = username)
                authCacheDataSource.observeAccessToken().first()?.let { accessToken ->
                    authCacheDataSource.storeAccessToken(
                        accessToken.copy(
                            turn = accessToken.turn?.copy(
                                isComplete = false
                            )
                        )
                    )
                }
            }.getOrThrow()

    override suspend fun logoutCurrentUser(): String {
        val username = authCacheDataSource.observeAccessToken().first()?.username.orEmpty()

        return authRemoteDataSource.logout(username)
            .onSuccess {
                authCacheDataSource.observeAccessToken().first()?.let { accessToken ->
                    authCacheDataSource.storeAccessToken(
                        accessToken.copy(
                            turn = accessToken.turn?.copy(
                                isComplete = true
                            )
                        )
                    )
                }
            }.getOrThrow()
    }

    override suspend fun deleteAccessToken() = authCacheDataSource.deleteAccessToken()

    override suspend fun deleteAccessTokenByUsername(username: String) =
        authCacheDataSource.deleteAccessTokenByUsername(username = username)
}
