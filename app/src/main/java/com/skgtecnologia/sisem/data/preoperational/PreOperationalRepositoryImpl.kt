package com.skgtecnologia.sisem.data.preoperational

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalRemoteDataSource
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import javax.inject.Inject

class PreOperationalRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val preOperationalRemoteDataSource: PreOperationalRemoteDataSource
) : PreOperationalRepository {

    override suspend fun getPreOperationalScreen(): ScreenModel {
        val role = OperationRole.getRoleByName(authCacheDataSource.retrieveAccessToken()?.role.orEmpty())
        checkNotNull(role)

        return preOperationalRemoteDataSource.getPreOperationalScreen(role).getOrThrow()
    }

    override suspend fun sendPreOperational() =
        preOperationalRemoteDataSource.sendPreOperational().getOrThrow()
}
