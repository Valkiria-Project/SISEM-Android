package com.skgtecnologia.sisem.data.operation

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.operation.remote.OperationRemoteDataSource
import com.skgtecnologia.sisem.domain.authcards.model.OperationModel
import com.skgtecnologia.sisem.domain.operation.OperationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource,
    private val operationRemoteDataSource: OperationRemoteDataSource
) : OperationRepository {

    override suspend fun getOperationConfig(serial: String): OperationModel =
        operationRemoteDataSource.getOperationConfig(serial)
            .onSuccess {
                operationCacheDataSource.storeOperationConfig(it)
            }.getOrThrow()

    override suspend fun logoutTurn(username: String): String {
        val turn = authCacheDataSource.observeAccessToken().first()?.turn
        val turnId = turn?.id?.toString().orEmpty()
        val idEmployed =
            authCacheDataSource.retrieveAccessTokenByUsername(username).userId.toString()
        val code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty()

        return operationRemoteDataSource.logoutTurn(
            username = username,
            idTurn = turnId,
            idEmployed = idEmployed,
            vehicleCode = code
        ).onSuccess {
            authCacheDataSource.deleteAccessTokenByUsername(username = username)
            authCacheDataSource.observeAccessToken().first()?.let { accessToken ->
                authCacheDataSource.storeAccessToken(
                    accessToken.copy(
                        turn = accessToken.turn?.copy(
                            isComplete = false
                        )
                    )
                )
            }
        }.getOrThrow()
    }

    override suspend fun observeOperationConfig(): Flow<OperationModel?> =
        operationCacheDataSource.observeOperationConfig()
}
