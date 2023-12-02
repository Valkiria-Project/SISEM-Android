package com.skgtecnologia.sisem.domain.stretcherretention

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface StretcherRetentionRepository {

    suspend fun getPreStretcherRetentionScreen(): ScreenModel

    suspend fun getStretcherRetentionScreen(
        idAph: String
    ): ScreenModel

    suspend fun saveStretcherRetention(
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    )

    suspend fun getStretcherRetentionViewScreen(
        idAph: String
    ): ScreenModel
}
