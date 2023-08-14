package com.skgtecnologia.sisem.data.preoperational

import com.skgtecnologia.sisem.data.preoperational.remote.PreOperationalRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.preoperational.PreOperationalRepository
import javax.inject.Inject

class PreOperationalRepositoryImpl @Inject constructor(
    private val preOperationalRemoteDataSource: PreOperationalRemoteDataSource
) : PreOperationalRepository {

    override suspend fun getPreOperationalScreen(): ScreenModel =
        preOperationalRemoteDataSource.getPreOperationalScreen().getOrThrow()

    override suspend fun savePreOperational() =
        preOperationalRemoteDataSource.savePreOperational().getOrThrow()
}
