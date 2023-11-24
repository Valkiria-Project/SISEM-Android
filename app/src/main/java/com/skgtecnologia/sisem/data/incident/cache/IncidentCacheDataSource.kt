package com.skgtecnologia.sisem.data.incident.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.incident.cache.dao.IncidentDao
import com.skgtecnologia.sisem.data.incident.cache.model.mapToCache
import com.skgtecnologia.sisem.data.incident.cache.model.mapToDomain
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class IncidentCacheDataSource @Inject constructor(
    private val incidentDao: IncidentDao
) {

    suspend fun storeIncident(incidentUiModel: IncidentUiModel) =
        incidentDao.insertIncident(incidentUiModel.mapToCache())

    @CheckResult
    fun observeActiveIncident(): Flow<IncidentUiModel> = incidentDao.observeActiveIncident()
        .filterNotNull()
        .map {
            it.mapToDomain()
        }
        .catch { throwable ->
            error("error observing the active Incident ${throwable.localizedMessage}")
        }
        .flowOn(Dispatchers.IO)
}
