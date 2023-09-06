package com.skgtecnologia.sisem.domain.report.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.ReportRepository
import javax.inject.Inject

class GetAddReportScreen @Inject constructor(
    private val reportRepository: ReportRepository
) {

    @CheckResult
    suspend operator fun invoke(serial: String): Result<ScreenModel> = resultOf {
        reportRepository.getAddReportScreen(serial)
    }
}
