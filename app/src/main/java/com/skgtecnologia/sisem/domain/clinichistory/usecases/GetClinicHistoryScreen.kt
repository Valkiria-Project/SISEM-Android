package com.skgtecnologia.sisem.domain.clinichistory.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.clinichistory.ClinicHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetClinicHistoryScreen @Inject constructor(
    private val clinicHistoryRepository: ClinicHistoryRepository
) {

    @CheckResult
    suspend operator fun invoke(
        serial: String,
        incidentCode: String,
        patientId: String
    ): Result<ScreenModel> = resultOf {
        clinicHistoryRepository.getClinicHistoryScreen(
            serial = serial,
            incidentCode = incidentCode,
            patientId = patientId
        )
    }
}
