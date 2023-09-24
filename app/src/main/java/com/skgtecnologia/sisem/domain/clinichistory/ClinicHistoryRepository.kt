package com.skgtecnologia.sisem.domain.clinichistory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel

interface ClinicHistoryRepository {

    suspend fun getClinicHistoryScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel
}
