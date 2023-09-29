package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.banner.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class MedicalHistoryRemoteDataSource @Inject constructor(
    private val medicalHistoryApi: MedicalHistoryApi,
    private val errorModelFactory: ErrorModelFactory
) {

    suspend fun getMedicalHistoryScreen(
        serial: String,
        code: String,
        turnId: String,
        incidentCode: String,
        patientId: String
    ): Result<ScreenModel> = apiCall(errorModelFactory) {
        medicalHistoryApi.getMedicalHistoryScreen(
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
}
