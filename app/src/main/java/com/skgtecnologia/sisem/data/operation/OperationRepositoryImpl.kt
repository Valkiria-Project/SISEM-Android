package com.skgtecnologia.sisem.data.operation

import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.skgtecnologia.sisem.domain.authcards.model.ConfigModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(
    private val operationCacheDataSource: OperationCacheDataSource,
    private val operationRemoteDataSource: OperationRemoteDataSource
) : OperationRepository {

    override suspend fun getConfig(): ConfigModel =
        operationRemoteDataSource.getConfig()
            .onSuccess {
                operationCacheDataSource.storeConfig(it)
            }.getOrThrow()

    override suspend fun retrieveConfig(): ConfigModel? = operationCacheDataSource.retrieveConfig()

    override suspend fun storeAmbulanceCode(code: String) =
        operationCacheDataSource.storeAmbulanceCode(code)
}
