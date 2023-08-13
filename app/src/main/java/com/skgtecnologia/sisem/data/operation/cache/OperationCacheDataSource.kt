package com.skgtecnologia.sisem.data.operation.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import com.skgtecnologia.sisem.data.operation.cache.model.mapToCache
import com.skgtecnologia.sisem.data.operation.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import javax.inject.Inject

class OperationCacheDataSource @Inject constructor(
    private val operationDao: OperationDao
) {

    suspend fun storeConfig(configModel: ConfigModel) =
        operationDao.insertConfig(operationEntity = configModel.mapToCache())

    @CheckResult
    suspend fun retrieveConfig(): ConfigModel? = operationDao.getOperation()?.mapToDomain()

    suspend fun storeAmbulanceCode(code: String) {
        operationDao.getOperation()?.copy(
            ambulanceCode = code
        )?.let { currentOperation ->
            operationDao.updateAmbulanceCode(currentOperation)
        }
    }
}
