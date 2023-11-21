package com.skgtecnologia.sisem.data.incident

import com.skgtecnologia.sisem.data.incident.cache.IncidentCacheDataSource
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IncidentRepositoryImpl @Inject constructor(
    private val incidentCacheDataSource: IncidentCacheDataSource
) : IncidentRepository {

    override fun observeActiveIncident(): Flow<IncidentUiModel> =
        incidentCacheDataSource.observeActiveIncident()
}
