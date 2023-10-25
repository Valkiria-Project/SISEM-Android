package com.skgtecnologia.sisem.domain.medicalhistory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi

interface MedicalHistoryRepository {

    suspend fun getMedicalHistoryScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel

    suspend fun getVitalSignsScreen(): ScreenModel

    suspend fun getMedicineScreen(): ScreenModel

    @Suppress("LongParameterList")
    suspend fun sendMedicalHistory(
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
    )
}
