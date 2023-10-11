package com.skgtecnologia.sisem.domain.medicalhistory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface MedicalHistoryRepository {

    suspend fun getMedicalHistoryScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel

    suspend fun getVitalSignsScreen(): ScreenModel

    suspend fun sendMedicalHistory(
        humanBodyValues: List<Map<String, List<String>>>,
        segmentedValues: Map<String, String>,
        fieldsValue: Map<String, Boolean>,
        sliderValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        imageButtonSectionValues: Map<String, String>,
        vitalSigns: Map<String, List<String>>
    )
}
