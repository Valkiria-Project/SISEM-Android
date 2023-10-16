package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.MedicalHistoryBody
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

    suspend fun getVitalSignsScreen(): Result<ScreenModel> = apiCall(errorModelFactory) {
        medicalHistoryApi.getVitalSignsScreen(
            screenBody = ScreenBody(
                params = Params()
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    suspend fun getMedicineScreen(): Result<ScreenModel> = apiCall(errorModelFactory) {
        medicalHistoryApi.getMedicineScreen(
            screenBody = ScreenBody(
                params = Params()
            )
        )
    }.mapResult {
        it.mapToDomain()
    }

    @Suppress("LongParameterList")
    suspend fun sendMedicalHistory(
        idTurn: String,
        idAph: String,
        humanBodyValues: List<Map<String, List<String>>>,
        segmentedValues: Map<String, String>,
        fieldsValue: Map<String, Boolean>,
        sliderValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        imageButtonSectionValues: Map<String, String>,
        vitalSigns: Map<String, List<String>>
    ): Result<Unit> = apiCall(errorModelFactory) {
        medicalHistoryApi.sendMedicalHistory(
            medicalHistoryBody = MedicalHistoryBody(
                idTurn = idTurn,
                idAph = idAph,
                humanBodyValues = humanBodyValues,
                segmentedValues = segmentedValues,
                fieldsValue = fieldsValue,
                sliderValues = sliderValues,
                dropDownValues = dropDownValues,
                chipSelectionValues = chipSelectionValues,
                imageButtonSectionValues = imageButtonSectionValues,
                vitalSigns = vitalSigns
            )
        )
    }
}
