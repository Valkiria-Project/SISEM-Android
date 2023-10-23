package com.skgtecnologia.sisem.domain.medicalhistory.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import javax.inject.Inject

class SendMedicalHistory @Inject constructor(
    private val medicalHistoryRepository: MedicalHistoryRepository
) {

    @Suppress("LongParameterList")
    @CheckResult
    suspend operator fun invoke(
        humanBodyUiValues: List<HumanBodyUi>,
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
    ): Result<Unit> = resultOf {
        medicalHistoryRepository.sendMedicalHistory(
            humanBodyUiValues,
            segmentedValues,
            signatureValues,
            fieldsValue,
            sliderValues,
            dropDownValues,
            chipSelectionValues,
            chipOptionsValues,
            imageButtonSectionValues,
            vitalSigns,
            infoCardButtonValues
        )
    }
}
