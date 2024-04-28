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

    override suspend fun getOperationConfig(serial: String): OperationModel {
        val turnId = authCacheDataSource.observeAccessToken()
            .first()?.turn?.id?.toString().orEmpty()

        return operationRemoteDataSource.getOperationConfig(serial, turnId)
            .onSuccess {
                operationCacheDataSource.storeOperationConfig(it)
            }.getOrThrow()
    }

    override suspend fun logoutTurn(username: String): String {
        val turnId = authCacheDataSource.observeAccessToken()
            .first()?.turn?.id?.toString().orEmpty()
        val idEmployed = authCacheDataSource.retrieveAccessTokenByUsername(username)
            .userId.toString()
        val code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty()

        // FIXME: Persist the new turn returned updating the users
        return operationRemoteDataSource.logoutTurn(
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
