package com.skgtecnologia.sisem.data.medicalhistory

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.medicalhistory.remote.MedicalHistoryRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@Suppress("UnusedPrivateMember")
class MedicalHistoryRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource, // FIXME: UnusedPrivateMember
    private val medicalHistoryRemoteDataSource: MedicalHistoryRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : MedicalHistoryRepository {

    override suspend fun getMedicalHistoryScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel = medicalHistoryRemoteDataSource.getMedicalHistoryScreen(
        serial = serial,
        code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty(),
        turnId = "1", // authCacheDataSource.observeAccessToken().first()?.turn?.id.orEmpty(),
        incidentCode = incidentCode,
        patientId = patientId
    ).getOrThrow()

    override suspend fun getVitalSignsScreen(): ScreenModel =
        medicalHistoryRemoteDataSource.getVitalSignsScreen().getOrThrow()

    override suspend fun getMedicineScreen(): ScreenModel =
        medicalHistoryRemoteDataSource.getMedicineScreen().getOrThrow()

    override suspend fun sendMedicalHistory(
        humanBodyValues: List<Map<String, List<String>>>,
        segmentedValues: Map<String, String>,
        fieldsValue: Map<String, Boolean>,
        sliderValues: Map<String, String>,
        dropDownValues: Map<String, String>,
        chipSelectionValues: Map<String, String>,
        imageButtonSectionValues: Map<String, String>,
        vitalSigns: Map<String, List<String>>
    ) = medicalHistoryRemoteDataSource.sendMedicalHistory(
        idTurn = "1", // authCacheDataSource.observeAccessToken().first()?.turn?.id.orEmpty(),
        idAph = "1", // FIXME: where is this value?
        humanBodyValues = humanBodyValues,
        segmentedValues = segmentedValues,
        fieldsValue = fieldsValue,
        sliderValues = sliderValues,
        dropDownValues = dropDownValues,
        chipSelectionValues = chipSelectionValues,
        imageButtonSectionValues = imageButtonSectionValues,
        vitalSigns = vitalSigns
    ).getOrThrow()
}
