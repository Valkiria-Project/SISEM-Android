package com.skgtecnologia.sisem.data.incident.cache

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.data.incident.cache.dao.IncidentDao
import com.skgtecnologia.sisem.data.incident.cache.model.mapToCache
import com.skgtecnologia.sisem.data.incident.cache.model.mapToUi
import com.valkiria.uicomponents.bricks.notification.model.TransmiNotification
import com.valkiria.uicomponents.bricks.notification.model.getTransmiNotificationRawDataByType
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IncidentCacheDataSource @Inject constructor(
    private val incidentDao: IncidentDao
) {

    suspend fun storeIncident(incidentUiModel: IncidentUiModel) =
        incidentDao.insertIncident(incidentUiModel.mapToCache())

    suspend fun updateTransmiStatus(
        incidentId: Long,
        transmiNotifications: List<TransmiNotification>
    ) {
        incidentDao.updateTransmiStatus(
            incidentId,
            transmiNotifications.map { getTransmiNotificationRawDataByType(it) }
        )
    }

    @CheckResult
    fun observeActiveIncident(): Flow<IncidentUiModel?> = incidentDao.observeActiveIncident()
        .distinctUntilChanged()
        .map {
            it?.mapToUi()
        }
        .catch { throwable ->
            error("error observing the active Incident ${throwable.localizedMessage}")
        }
        .flowOn(Dispatchers.IO)
}
