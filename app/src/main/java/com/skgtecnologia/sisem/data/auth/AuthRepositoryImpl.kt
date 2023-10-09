package com.skgtecnologia.sisem.data.auth

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.auth.remote.AuthRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : AuthRepository {

    override suspend fun authenticate(username: String, password: String): AccessTokenModel =
        authRemoteDataSource.authenticate(
            username = username,
            password = password,
            code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty(),
            turnId = authCacheDataSource.observeAccessToken().first()?.turn?.id?.toString()
                .orEmpty()
        ).onSuccess { accessTokenModel ->
            if (accessTokenModel.isAdmin) {
                getAllAccessTokens().forEach { accessToken ->
                    logout(accessToken.username)
                }
            }

            authCacheDataSource.storeAccessToken(accessTokenModel)
        }.getOrThrow()

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
