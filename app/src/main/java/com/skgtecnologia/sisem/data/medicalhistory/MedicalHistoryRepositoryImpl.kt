package com.skgtecnologia.sisem.data.medicalhistory

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.medicalhistory.remote.MedicalHistoryRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.medicalhistory.MedicalHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MedicalHistoryRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
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
        turnId = authCacheDataSource.observeAccessToken().first()?.turn?.id.toString(),
        incidentCode = incidentCode,
        patientId = patientId
    ).getOrThrow()
}
