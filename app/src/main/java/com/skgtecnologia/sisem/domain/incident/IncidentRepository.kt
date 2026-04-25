package com.skgtecnologia.sisem.domain.incident

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.flow.Flow

interface IncidentRepository {

    suspend fun getIncident()

    fun observeActiveIncident(): Flow<IncidentUiModel?>

    suspend fun getIncidentScreen(): ScreenModel
}
