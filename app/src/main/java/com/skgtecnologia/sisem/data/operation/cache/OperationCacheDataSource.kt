package com.skgtecnologia.sisem.data.operation.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import com.skgtecnologia.sisem.data.operation.cache.model.mapToCache
import com.skgtecnologia.sisem.data.operation.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import javax.inject.Inject

class OperationCacheDataSource @Inject constructor(
    private val operationDao: OperationDao
) {

    suspend fun storeOperationConfig(operationModel: OperationModel) =
        operationDao.insertConfig(operationEntity = operationModel.mapToCache())

    @CheckResult
    suspend fun retrieveOperationConfig(): OperationModel? =
        operationDao.getOperation()?.mapToDomain()
}
