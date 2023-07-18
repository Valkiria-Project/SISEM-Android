package com.skgtecnologia.sisem.data.login.cache

import com.skgtecnologia.sisem.data.login.cache.dao.AccessTokenDao
import com.skgtecnologia.sisem.data.login.cache.model.mapToCache
import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import javax.inject.Inject

class LoginCacheDataSource @Inject constructor(
    private val accessTokenDao: AccessTokenDao,
) {

    suspend fun storeAccessToken(accessTokenModel: AccessTokenModel) {
        accessTokenDao.insertAccessToken(accessTokenEntity = accessTokenModel.mapToCache())
    }
}
