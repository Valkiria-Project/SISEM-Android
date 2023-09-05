package com.skgtecnologia.sisem.data.preoperational

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalRemoteDataSource
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import javax.inject.Inject

class PreOperationalRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val preOperationalRemoteDataSource: PreOperationalRemoteDataSource
) : PreOperationalRepository {

    override suspend fun getPreOperationalScreen(): ScreenModel {
        val role = OperationRole.getRoleByName(
            authCacheDataSource.retrieveAccessToken()?.role.orEmpty()
        )
        checkNotNull(role)

        return preOperationalRemoteDataSource.getPreOperationalScreen(role).getOrThrow()
    }

    override suspend fun sendPreOperational(
        findings: Map<String, Boolean>,
        inventoryValues: Map<String, Int>,
        fieldsValues: Map<String, String>,
        novelties: List<Novelty>
    ) {
        preOperationalRemoteDataSource.sendPreOperational(
            role = checkNotNull(
                OperationRole.getRoleByName(
                    authCacheDataSource.retrieveAccessToken()?.role.orEmpty()
                )
            ),
            idTurn = authCacheDataSource.retrieveAccessToken()?.turn?.id?.toString().orEmpty(),
            findings = findings,
            inventoryValues = inventoryValues,
            fieldsValues = fieldsValues,
            novelties = novelties
        ).getOrThrow()
    }
}
