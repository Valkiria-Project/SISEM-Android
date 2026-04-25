package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import javax.inject.Inject

class DeleteAphFile @Inject constructor(
    private val medicalHistoryRepository: MedicalHistoryRepository
) {

    suspend operator fun invoke(
        idAph: String,
        fileName: String
    ): Result<Unit> = resultOf {
        medicalHistoryRepository.deleteAphFile(idAph, fileName)
    }
}
