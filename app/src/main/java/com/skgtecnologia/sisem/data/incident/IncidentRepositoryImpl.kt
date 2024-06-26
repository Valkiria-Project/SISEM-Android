package com.skgtecnologia.sisem.data.incident

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class IncidentRepositoryImpl @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val incidentCacheDataSource: IncidentCacheDataSource,
    private val incidentRemoteDataSource: IncidentRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) : IncidentRepository {

    override suspend fun getIncident() {
        val activeIncident = observeActiveIncident().first()

        incidentRemoteDataSource.getIncidentInfo(
            idIncident = activeIncident?.incident?.id.toString(),
            idTurn = authCacheDataSource.observeAccessToken()
                .first()
                ?.turn
                ?.id
                ?.toString()
                .orEmpty(),
            codeVehicle = operationCacheDataSource.observeOperationConfig()
                .first()
                ?.vehicleCode
                .orEmpty()
        ).onSuccess {
            val incident = it.copy(
                incidentPriority = activeIncident?.incidentPriority,
                latitude = activeIncident?.latitude,
                longitude = activeIncident?.longitude
            )
            incidentCacheDataSource.storeIncident(incident)
        }
    }

    override fun observeActiveIncident(): Flow<IncidentUiModel?> =
        incidentCacheDataSource.observeActiveIncident()

    override suspend fun getIncidentScreen(): ScreenModel =
        incidentRemoteDataSource.getIncidentScreen(
            operationCacheDataSource.observeOperationConfig().first()?.vehicleCode.orEmpty()
        ).getOrThrow()
}
