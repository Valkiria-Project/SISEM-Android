package com.skgtecnologia.sisem.domain.medicalhistory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface MedicalHistoryRepository {

    suspend fun getMedicalHistoryScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel
}
