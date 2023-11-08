package com.skgtecnologia.sisem.domain.stretcherretention

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface StretcherRetentionRepository {

    suspend fun getStretcherRetentionScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel

    suspend fun saveStretcherRetention(
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    )
}
