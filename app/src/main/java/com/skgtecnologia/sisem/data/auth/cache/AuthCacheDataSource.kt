package com.skgtecnologia.sisem.data.auth.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.mapToCache
import com.skgtecnologia.sisem.data.auth.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class AuthCacheDataSource @Inject constructor(
    private val accessTokenDao: AccessTokenDao,
) {

    suspend fun storeAccessToken(accessTokenModel: AccessTokenModel) {
        accessTokenDao.insertAccessToken(accessTokenEntity = accessTokenModel.mapToCache())
    }

    @CheckResult
    fun observeAccessToken(): Flow<AccessTokenModel?> = accessTokenDao.observeAccessToken()
        .map {
            it?.mapToDomain()
        }
        .catch { throwable ->
            error("error observing the AccessToken ${throwable.localizedMessage}")
        }
        .flowOn(Dispatchers.IO)

    @CheckResult
    suspend fun retrieveAllAccessTokens(): List<AccessTokenModel> =
        accessTokenDao.getAllAccessTokens().map { it.mapToDomain() }

    @CheckResult
    suspend fun retrieveAccessTokenByUsername(username: String): AccessTokenModel =
        accessTokenDao.getAccessTokenByUsername(username).mapToDomain()

    suspend fun updatePreOperationalStatus(role: String, status: Boolean) {
        accessTokenDao.updatePreOperationalStatus(role, status)
    }

    suspend fun updateTurn(turnId: String, previousTurn: String) {
        accessTokenDao.updateTurn(turnId, previousTurn)
    }

    suspend fun deleteAccessToken() {
        accessTokenDao.deleteAccessToken()
    }

    @CheckResult
    suspend fun deleteAccessTokenByUsername(username: String) =
        accessTokenDao.deleteAccessTokenByUsername(username = username)
}
