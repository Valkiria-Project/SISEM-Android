package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import javax.inject.Inject

class SendMedicalHistory @Inject constructor(
    private val medicalHistoryRepository: MedicalHistoryRepository
) {

    @Suppress("LongParameterList")
    @CheckResult
    suspend operator fun invoke(
        humanBodyValues: List<Map<String, List<String>>>,
        segmentedValues: Map<String, String>,
        fieldsValue: Map<String, Boolean>,
        sliderValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        imageButtonSectionValues: Map<String, String>,
        vitalSigns: Map<String, List<String>>
    ): Result<Unit> = resultOf {
        medicalHistoryRepository.sendMedicalHistory(
            humanBodyValues,
            segmentedValues,
            fieldsValue,
            sliderValues,
            dropDownValues,
            chipSelectionValues,
            imageButtonSectionValues,
            vitalSigns
        )
    }
}
