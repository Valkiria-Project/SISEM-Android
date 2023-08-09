package com.skgtecnologia.sisem.data.auth.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.auth.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.auth.cache.model.mapToCache
import com.skgtecnologia.sisem.data.auth.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import javax.inject.Inject

class AuthCacheDataSource @Inject constructor(
    private val accessTokenDao: AccessTokenDao,
) {

    suspend fun storeAccessToken(accessTokenModel: AccessTokenModel) {
        accessTokenDao.insertAccessToken(accessTokenEntity = accessTokenModel.mapToCache())
    }

    @CheckResult
    suspend fun retrieveAccessToken(): AccessTokenModel? =
        accessTokenDao.getAccessToken()?.mapToDomain()

    @CheckResult
    suspend fun getAllAccessTokenModel(): List<AccessTokenModel> =
        accessTokenDao.getAllAccessToken().map { it.mapToDomain() }
}
