package com.skgtecnologia.sisem.data.medicalhistory

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.medicalhistory.remote.MedicalHistoryRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MedicalHistoryRepositoryImpl @Inject constructor(
    private val medicalHistoryRemoteDataSource: MedicalHistoryRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : MedicalHistoryRepository {

    override suspend fun getMedicalHistoryScreen(
        idAph: String
    ): ScreenModel = medicalHistoryRemoteDataSource.getMedicalHistoryScreen(
        code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty(),
        idAph = idAph
    ).getOrThrow()

    override suspend fun getVitalSignsScreen(): ScreenModel =
        medicalHistoryRemoteDataSource.getVitalSignsScreen().getOrThrow()

    override suspend fun getMedicineScreen(): ScreenModel =
        medicalHistoryRemoteDataSource.getMedicineScreen().getOrThrow()

    override suspend fun sendMedicalHistory(
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
        infoCardButtonValues: List<Map<String, String>>
    ) = medicalHistoryRemoteDataSource.sendMedicalHistory(
        // FIXME: authCacheDataSource.observeAccessToken().first()?.turn?.id.orEmpty(),
        idTurn = "1",
        // FIXME: Complete with notification work
        idAph = if (BuildConfig.BUILD_TYPE == "debug") "24" else "14", // FIXME: 14 QA & 24 DEV
        humanBodyValues = humanBodyValues,
        segmentedValues = segmentedValues,
        signatureValues = signatureValues,
        fieldsValue = fieldsValue,
        sliderValues = sliderValues,
        dropDownValues = dropDownValues,
        chipSelectionValues = chipSelectionValues,
        chipOptionsValues = chipOptionsValues,
        imageButtonSectionValues = imageButtonSectionValues,
        vitalSigns = vitalSigns,
        infoCardButtonValues = infoCardButtonValues
    ).getOrThrow()
}
