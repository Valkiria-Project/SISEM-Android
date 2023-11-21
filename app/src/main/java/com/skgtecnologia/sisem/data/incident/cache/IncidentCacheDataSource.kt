package com.skgtecnologia.sisem.data.incident.cache

import com.skgtecnologia.sisem.data.incident.cache.dao.IncidentDao
import com.skgtecnologia.sisem.data.incident.cache.model.mapToCache
import com.skgtecnologia.sisem.domain.incident.model.IncidentModel
import javax.inject.Inject

class IncidentCacheDataSource @Inject constructor(
    private val incidentDao: IncidentDao
) {

    suspend fun storeIncident(incidentModel: IncidentModel) =
        incidentDao.insertIncident(incidentModel.mapToCache())
}
