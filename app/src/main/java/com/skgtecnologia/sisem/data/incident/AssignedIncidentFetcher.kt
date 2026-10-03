package com.skgtecnologia.sisem.data.incident

import com.skgtecnologia.sisem.data.auth.cache.AuthCacheDataSource
import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.data.incident.remote.IncidentRemoteDataSource
import com.skgtecnologia.sisem.data.operation.cache.OperationCacheDataSource
import com.valkiria.uicomponents.components.incident.model.IncidentPriority
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/** What the assignment push says about the incident, enough to fetch the rest. */
data class IncidentAssignment(
    val incidentNumber: String,
    val incidentPriority: String,
    val geolocation: String
)

/** Fetches an assigned incident and makes it the active one. */
class AssignedIncidentFetcher @Inject constructor(
    private val authCacheDataSource: AuthCacheDataSource,
    private val incidentCacheDataSource: IncidentCacheDataSource,
    private val incidentRemoteDataSource: IncidentRemoteDataSource,
    private val operationCacheDataSource: OperationCacheDataSource
) {

    suspend fun fetch(assignment: IncidentAssignment): Result<Unit> =
        incidentRemoteDataSource.getIncidentInfo(
            idIncident = assignment.incidentNumber,
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
        ).map {
            // "longitude,latitude". A malformed value leaves the map without a pin rather than
            // losing the incident.
            val coordinates = assignment.geolocation.split(",").map { part -> part.trim().toDoubleOrNull() }
            val incident = it.copy(
                incidentPriority = IncidentPriority.getPriority(assignment.incidentPriority),
                latitude = coordinates.getOrNull(1),
                longitude = coordinates.getOrNull(0)
            )
            incidentCacheDataSource.storeIncident(incident)
        }
}
