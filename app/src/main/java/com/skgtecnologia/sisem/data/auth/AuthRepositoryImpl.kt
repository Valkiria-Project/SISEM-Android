package com.skgtecnologia.sisem.data.auth

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.auth.remote.AuthRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : AuthRepository {

    override suspend fun authenticate(username: String, password: String): AccessTokenModel {
        val code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty()
        val turnId = authCacheDataSource.observeAccessToken()
            .first()
            ?.turn
            ?.id
            ?.toString()

        return authRemoteDataSource.authenticate(
            username = username,
            password = password,
            code = code,
            turnId = turnId.orEmpty()
        ).onSuccess { accessTokenModel ->
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

            authCacheDataSource.storeAccessToken(accessTokenModel)
        }.getOrThrow()
    }

    override suspend fun refreshToken(currentToken: AccessTokenModel): AccessTokenModel =
        authRemoteDataSource.refreshToken(refreshToken = currentToken.refreshToken)
            .mapResult { refreshTokenModel ->
                val token = currentToken.copy(
                    accessToken = refreshTokenModel.accessToken,
                    refreshToken = refreshTokenModel.refreshToken,
                    isAdmin = refreshTokenModel.isAdmin
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

    override suspend fun deleteAccessToken() = authCacheDataSource.deleteAccessToken()

    override suspend fun deleteAccessTokenByUsername(username: String) =
        authCacheDataSource.deleteAccessTokenByUsername(username = username)
}
