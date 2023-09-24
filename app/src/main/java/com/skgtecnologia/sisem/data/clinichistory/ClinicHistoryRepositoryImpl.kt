package com.skgtecnologia.sisem.data.clinichistory

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.clinichistory.remote.ClinicHistoryRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.clinichistory.ClinicHistoryRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class ClinicHistoryRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val clinicHistoryRemoteDataSource: ClinicHistoryRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : ClinicHistoryRepository {

    override suspend fun getClinicHistoryScreen(
        serial: String,
        incidentCode: String,
        patientId: String
    ): ScreenModel = clinicHistoryRemoteDataSource.getClinicHistoryScreen(
        serial = serial,
        code = operationCacheDataSource.retrieveOperationConfig()?.vehicleCode.orEmpty(),
        turnId = "1", //authCacheDataSource.retrieveAccessToken()?.turn?.id?.toString().orEmpty(),
        incidentCode = incidentCode,
        patientId = patientId
    ).getOrThrow()
}
