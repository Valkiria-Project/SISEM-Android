package com.skgtecnologia.sisem.domain.incident.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveActiveIncident @Inject constructor(
    private val incidentRepository: IncidentRepository
) {

    @CheckResult
    operator fun invoke(): Flow<IncidentUiModel?> = incidentRepository.observeActiveIncident()
}
