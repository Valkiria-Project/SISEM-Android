package com.skgtecnologia.sisem.data.authcards.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.authcards.cache.dao.ConfigDao
import com.skgtecnologia.sisem.data.authcards.cache.model.mapToCache
import com.skgtecnologia.sisem.data.authcards.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import javax.inject.Inject

class AuthCardsCacheDataSource @Inject constructor(
    private val configDao: ConfigDao
) {

    suspend fun storeConfig(configModel: ConfigModel) {
        configDao.insertConfig(configEntity = configModel.mapToCache())
    }

    @CheckResult
    suspend fun retrieveConfig(): ConfigModel? =
        configDao.getConfig()?.mapToDomain()
}
