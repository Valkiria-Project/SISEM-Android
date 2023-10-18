package com.skgtecnologia.sisem.data.preoperational

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalRemoteDataSource
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreOperationalRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val operationCacheDataSource: OperationCacheDataSource,
    private val preOperationalRemoteDataSource: PreOperationalRemoteDataSource
) : PreOperationalRepository {

    override suspend fun getPreOperationalScreen(androidId: String): ScreenModel {
        val accessToken = checkNotNull(authCacheDataSource.observeAccessToken())

        return preOperationalRemoteDataSource.getPreOperationalScreen(
            role = checkNotNull(OperationRole.getRoleByName(accessToken.first()?.role.orEmpty())),
            androidId = androidId,
            vehicleCode = operationCacheDataSource.observeOperationConfig()
                .first()?.vehicleCode.orEmpty(),
            idTurn = accessToken.first()?.turn?.id?.toString().orEmpty()
        ).getOrThrow()
    }

    override suspend fun getRole(): OperationRole = checkNotNull(
        OperationRole.getRoleByName(
            authCacheDataSource.observeAccessToken().first()?.role.orEmpty()
        )
    )

    override suspend fun sendPreOperational(
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>,
        novelties: List<Novelty>
    ) {
        val accessToken = checkNotNull(authCacheDataSource.observeAccessToken().first())
        val role = checkNotNull(OperationRole.getRoleByName(accessToken.role))
        val idTurn = accessToken.turn?.id?.toString().orEmpty()

        preOperationalRemoteDataSource.sendPreOperational(
            role = role,
            idTurn = idTurn,
            findings = findings,
            inventoryValues = inventoryValues,
            fieldsValues = fieldsValues
        ).onSuccess {
            authCacheDataSource.updatePreOperationalStatus(accessToken.role)
            preOperationalRemoteDataSource.sendFinding(
                role = role,
                idTurn = idTurn,
                novelties = novelties
            )
        }.getOrThrow()
    }
}
