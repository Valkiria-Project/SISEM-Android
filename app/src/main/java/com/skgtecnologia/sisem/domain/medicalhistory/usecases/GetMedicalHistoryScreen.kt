package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetMedicalHistoryScreen @Inject constructor(
    private val medicalHistoryRepository: MedicalHistoryRepository
) {

    @CheckResult
    suspend operator fun invoke(
        serial: String,
        incidentCode: String,
        patientId: String
    ): Result<ScreenModel> = resultOf {
        medicalHistoryRepository.getMedicalHistoryScreen(
            serial = serial,
            incidentCode = incidentCode,
            patientId = patientId
        )
    }
}
