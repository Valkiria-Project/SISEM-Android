package com.skgtecnologia.sisem.data.operation.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.operation.cache.dao.OperationDao
import com.skgtecnologia.sisem.data.operation.cache.model.mapToCache
import com.skgtecnologia.sisem.data.operation.cache.model.mapToDomain
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OperationCacheDataSource @Inject constructor(
    private val operationDao: OperationDao
) {

    suspend fun storeOperationConfig(operationModel: OperationModel) =
        operationDao.insertConfig(operationEntity = operationModel.mapToCache())

    @CheckResult
    fun observeOperationConfig(): Flow<OperationModel?> = operationDao.observeOperation()
        .map {
            it?.mapToDomain() ?: error("Operation config does not exist")
        }
        .catch { throwable ->
            error("error observing the Operation config ${throwable.localizedMessage}")
        }
        .flowOn(Dispatchers.IO)
}
