package com.skgtecnologia.sisem.data.stretcherretention

import com.skgtecnologia.sisem.data.stretcherretention.remote.StretcherRetentionRemoteDataSource
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.stretcherretention.StretcherRetentionRepository
import javax.inject.Inject

class StretcherRetentionRepositoryImpl @Inject constructor(
    private val stretcherRetentionRemoteDataSource: StretcherRetentionRemoteDataSource
) : StretcherRetentionRepository {

    override suspend fun getStretcherRetentionScreen(
        idAph: String
    ): ScreenModel = stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
        idAph = idAph
    ).getOrThrow()

    override suspend fun saveStretcherRetention(
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    ) = stretcherRetentionRemoteDataSource.saveStretcherRetention(
        idAph = "24", // FIXME: update with cache
        fieldsValue = fieldsValue,
        chipSelectionValues = chipSelectionValues
    ).getOrThrow()

    override suspend fun getStretcherRetentionViewScreen(
        idAph: String
    ): ScreenModel = stretcherRetentionRemoteDataSource.getStretcherRetentionScreen(
        idAph = idAph
    ).getOrThrow()
}
