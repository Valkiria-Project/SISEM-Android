package com.skgtecnologia.sisem.data.medicalhistory.remote

import com.skgtecnologia.sisem.commons.extensions.mapResult
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.MedicalHistoryBody
import com.skgtecnologia.sisem.data.medicalhistory.remote.model.mapToBody
import com.skgtecnologia.sisem.data.remote.extensions.apiCall
import com.skgtecnologia.sisem.data.remote.model.screen.Params
import com.skgtecnologia.sisem.data.remote.model.screen.ScreenBody
import com.skgtecnologia.sisem.data.remote.model.screen.mapToDomain
import com.skgtecnologia.sisem.domain.model.banner.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
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
        humanBodyValues: List<HumanBodyUi>,
        segmentedValues: Map<String, Boolean>,
        signatureValues: Map<String, String>,
        fieldsValue: Map<String, String>,
        sliderValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        chipOptionsValues: Map<String, List<String>>,
        imageButtonSectionValues: Map<String, String>,
        vitalSigns: Map<String, Map<String, String>>,
        infoCardButtonValues: List<Map<String, String>>
    ): Result<Unit> = apiCall(errorModelFactory) {
        medicalHistoryApi.sendMedicalHistory(
            medicalHistoryBody = MedicalHistoryBody(
                idTurn = idTurn,
                idAph = idAph,
                humanBodyValues = humanBodyValues.map { it.mapToBody() },
                segmentedValues = segmentedValues,
                signatureValues = signatureValues,
                fieldsValue = fieldsValue,
                sliderValues = sliderValues,
                dropDownValues = dropDownValues,
                chipSelectionValues = chipSelectionValues,
                chipOptionsValues = chipOptionsValues,
                imageButtonSectionValues = imageButtonSectionValues,
                vitalSigns = vitalSigns,
                infoCardButtonValues = infoCardButtonValues
            )
        )
    }
}
