package com.skgtecnologia.sisem.domain.incident.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.incident.IncidentRepository
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import javax.inject.Inject

class GetIncidentScreen @Inject constructor(
    private val incidentRepository: IncidentRepository
) {

    @CheckResult
    suspend operator fun invoke(): Result<ScreenModel> = resultOf {
        incidentRepository.getIncidentScreen()
    }
}
