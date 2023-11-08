package com.skgtecnologia.sisem.data.stretcherretention

import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.data.stretcherretention.remote.StretcherRetentionRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StretcherRetentionRepositoryImpl @Inject constructor(
    private val stretcherRetentionRemoteDataSource: StretcherRetentionRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : StretcherRetentionRepository {

    override suspend fun getStretcherRetentionScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel = stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
        serial = serial,
        code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty(),
        // FIXME: authCacheDataSource.observeAccessToken().first()?.turn?.id.orEmpty(),
        turnId = "1",
        incidentCode = incidentCode,
        patientId = patientId
    ).getOrThrow()

    override suspend fun saveStretcherRetention(
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    ) = stretcherRetentionRemoteDataSource.saveStretcherRetention(
        idAph = "24", // FIXME: update with cache
        fieldsValue = fieldsValue,
        chipSelectionValues = chipSelectionValues
    ).getOrThrow()
}
