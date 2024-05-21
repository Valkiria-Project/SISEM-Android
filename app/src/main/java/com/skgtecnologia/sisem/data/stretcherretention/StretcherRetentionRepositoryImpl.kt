package com.skgtecnologia.sisem.data.stretcherretention

import com.skgtecnologia.sisem.commons.extensions.validateOrThrow
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.stretcherretention.remote.StretcherRetentionRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import com.skgtecnologia.sisem.domain.stretcherretention.errors.StretchRetentionErrors
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StretcherRetentionRepositoryImpl @Inject constructor(
    private val incidentCacheDataSource: IncidentCacheDataSource,
    private val stretcherRetentionRemoteDataSource: StretcherRetentionRemoteDataSource
) : StretcherRetentionRepository {

    override suspend fun getPreStretcherRetentionScreen(): ScreenModel {
        val incidentId = incidentCacheDataSource.observeActiveIncident().first()?.incident?.id
        validateOrThrow(incidentId != null) { StretchRetentionErrors.NoIncidentId }

        return stretcherRetentionRemoteDataSource.getPreStretcherRetentionScreen(
            idIncident = incidentId.toString()
        ).getOrThrow()
    }

    override suspend fun getStretcherRetentionScreen(
        idAph: String
    ): ScreenModel = stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
        idAph = idAph
    ).getOrThrow()

    override suspend fun saveStretcherRetention(
        idAph: String,
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    ) = stretcherRetentionRemoteDataSource.saveStretcherRetention(
        idAph = idAph,
        fieldsValue = fieldsValue,
        chipSelectionValues = chipSelectionValues
    ).getOrThrow()

    override suspend fun getStretcherRetentionViewScreen(
        idAph: String
    ): ScreenModel = stretcherRetentionRemoteDataSource.getStretcherRetentionViewScreen(
        idAph = idAph
    ).getOrThrow()
}
