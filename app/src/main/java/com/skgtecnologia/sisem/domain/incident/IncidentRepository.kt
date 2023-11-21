package com.skgtecnologia.sisem.domain.incident

import com.skgtecnologia.sisem.domain.incident.model.IncidentModel
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    fun observeActiveIncident(): Flow<IncidentModel?>
}
