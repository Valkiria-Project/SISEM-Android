package com.skgtecnologia.sisem.data.stretcherretention.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.data.stretcherretention.remote.model.StretcherRetentionBody
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class StretcherRetentionRemoteDataSource @Inject constructor(
    private val stretcherRetentionApi: StretcherRetentionApi
) {

    suspend fun getStretcherRetentionScreen(
        serial: String,
        code: String,
        turnId: String,
        incidentCode: String,
        patientId: String
    ): Result<ScreenModel> = apiCall {
        stretcherRetentionApi.getStretcherRetentionScreen(
            screenBody = ScreenBody(
                params = Params(
                    serial = serial,
                    code = code,
                    turnId = turnId,
                    incidentCode = incidentCode,
                    patientId = patientId
                )
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun saveStretcherRetention(
        idAph: String,
        fieldsValue: Map<String, String>,
        chipSelectionValues: Map<String, String>
    ): Result<Unit> = apiCall {
        stretcherRetentionApi.saveStretcherRetention(
            stretcherRetentionBody = StretcherRetentionBody(
                idAph = idAph,
                fieldsValues = fieldsValue,
                chipSelectionValues = chipSelectionValues
            )
        )
    }
}