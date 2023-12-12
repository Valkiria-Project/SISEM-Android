package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import javax.inject.Inject

class SaveAphFiles @Inject constructor(
    private val medicalHistoryRepository: MedicalHistoryRepository
) {

    suspend operator fun invoke(idAph: String, images: List<ImageModel>): Result<Unit> = resultOf {
        medicalHistoryRepository.saveAphFiles(idAph, images)
    }
}
