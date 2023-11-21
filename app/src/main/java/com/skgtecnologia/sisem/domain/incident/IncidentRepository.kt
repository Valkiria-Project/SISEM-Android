package com.skgtecnologia.sisem.domain.incident

import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    fun observeActiveIncident(): Flow<IncidentUiModel>
}
