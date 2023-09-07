package com.skgtecnologia.sisem.data.preoperational

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalRemoteDataSource
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import javax.inject.Inject

class PreOperationalRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource,
    private val preOperationalRemoteDataSource: PreOperationalRemoteDataSource
) : PreOperationalRepository {

    override suspend fun getPreOperationalScreen(androidId: String): ScreenModel {
        val accessToken = checkNotNull(authCacheDataSource.retrieveAccessToken())

        return preOperationalRemoteDataSource.getPreOperationalScreen(
            role = checkNotNull(OperationRole.getRoleByName(accessToken.role)),
            androidId = androidId,
            vehicleCode = operationCacheDataSource.retrieveOperationConfig()?.vehicleCode.orEmpty(),
            idTurn = accessToken.turn?.id?.toString().orEmpty()
        ).getOrThrow()
    }

    override suspend fun sendPreOperational(
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>,
        novelties: List<Novelty>
    ) {
        val accessToken = checkNotNull(authCacheDataSource.retrieveAccessToken())

        preOperationalRemoteDataSource.sendPreOperational(
            role = checkNotNull(OperationRole.getRoleByName(accessToken.role)),
            idTurn = accessToken.turn?.id?.toString().orEmpty(),
            findings = findings,
            inventoryValues = inventoryValues,
            fieldsValues = fieldsValues,
            novelties = novelties
        ).onSuccess {
            authCacheDataSource.updatePreOperationalStatus(accessToken.role)
        }.getOrThrow()
    }
}
