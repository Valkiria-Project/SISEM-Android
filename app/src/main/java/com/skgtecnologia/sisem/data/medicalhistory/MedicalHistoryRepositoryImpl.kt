package com.skgtecnologia.sisem.data.medicalhistory

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.model.mapToCache
import com.skgtecnologia.sisem.data.medicalhistory.remote.MedicalHistoryRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MedicalHistoryRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val incidentCacheDataSource: IncidentCacheDataSource,
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

    override suspend fun getMedicineScreen(serial: String): ScreenModel =
        medicalHistoryRemoteDataSource.getMedicineScreen(serial).getOrThrow()

    override suspend fun sendMedicalHistory(
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
        images: List<ImageModel>
    ) = medicalHistoryRemoteDataSource.sendMedicalHistory(
        idTurn = authCacheDataSource.observeAccessToken().first()?.turn?.id.toString(),
        idAph = idAph,
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
    ).onSuccess {
        if (images.isNotEmpty()) {
            medicalHistoryRemoteDataSource.saveAphFiles(idAph, images).getOrThrow()
        }

        val incident = checkNotNull(incidentCacheDataSource.observeActiveIncident().first())
        val patients = incident.patients.map {
            val patient = if (it.idAph.toString() == idAph) it.copy(disabled = true) else it
            patient
        }
        incidentCacheDataSource.updatePatientStatus(incident.id!!, patients)
    }.getOrThrow()

    override suspend fun saveAphFiles(idAph: String, images: List<ImageModel>) =
        medicalHistoryRemoteDataSource.saveAphFiles(idAph, images).getOrThrow()

    override suspend fun getMedicalHistoryViewScreen(idAph: String): ScreenModel =
        medicalHistoryRemoteDataSource.getMedicalHistoryViewScreen(
            code = operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty(),
            idAph = idAph
        ).getOrThrow()
}
