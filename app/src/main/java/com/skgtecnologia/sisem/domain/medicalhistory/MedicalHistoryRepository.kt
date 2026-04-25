package com.skgtecnologia.sisem.domain.medicalhistory

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi

interface MedicalHistoryRepository {

    suspend fun getMedicalHistoryScreen(idAph: String): ScreenModel

    suspend fun getVitalSignsScreen(): ScreenModel

    suspend fun getMedicineScreen(serial: String): ScreenModel

    @Suppress("LongParameterList")
    suspend fun sendMedicalHistory(
        idAph: String,
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
        infoCardButtonValues: List<Map<String, String>>,
        images: List<ImageModel>,
        description: String? = null
    )

    suspend fun saveAphFiles(idAph: String, images: List<ImageModel>, description: String?)

    suspend fun getMedicalHistoryViewScreen(idAph: String): ScreenModel

    suspend fun deleteAphFile(idAph: String, fileName: String)
}
