package com.skgtecnologia.sisem.data.operation

import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(
    private val operationCacheDataSource: OperationCacheDataSource,
    private val operationRemoteDataSource: OperationRemoteDataSource
) : OperationRepository {

    override suspend fun getOperationConfig(serial: String): OperationModel =
        operationRemoteDataSource.getOperationConfig(serial)
            .onSuccess {
                operationCacheDataSource.storeOperationConfig(it)
            }.getOrThrow()

    override suspend fun observeOperationConfig(): Flow<OperationModel?> =
        operationCacheDataSource.observeOperationConfig()
}
