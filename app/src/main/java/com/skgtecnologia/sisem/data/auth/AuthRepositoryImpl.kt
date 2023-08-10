package com.skgtecnologia.sisem.data.auth

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.auth.remote.AuthRemoteDataSource
import com.skgtecnologia.sisem.domain.auth.AuthRepository
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun authenticate(username: String, password: String): AccessTokenModel =
        authRemoteDataSource.authenticate(
            username = username,
            password = password,
            turnId = authCacheDataSource.retrieveAccessToken()?.turn?.id?.toString().orEmpty()
        ).onSuccess { accessTokenModel ->
            authCacheDataSource.storeAccessToken(accessTokenModel)
        }.getOrThrow()

    override suspend fun getAccessToken(): String? =
        authCacheDataSource.retrieveAccessToken()?.accessToken

    override suspend fun getAllAccessTokens(): List<AccessTokenModel> =
        authCacheDataSource.retrieveAllAccessTokens()

    override suspend fun logout(username: String): String =
        authRemoteDataSource.logout(username).getOrThrow()
}
