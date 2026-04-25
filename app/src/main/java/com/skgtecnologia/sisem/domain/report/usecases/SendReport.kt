package com.skgtecnologia.sisem.domain.report.usecases

import androidx.annotation.CheckResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.domain.report.ReportRepository
import com.skgtecnologia.sisem.domain.report.model.ImageModel
import javax.inject.Inject

class SendReport @Inject constructor(
    private val reportRepository: ReportRepository
) {

    @CheckResult
    suspend operator fun invoke(
        roleName: String?,
        topic: String,
        description: String,
        images: List<ImageModel>
    ): Result<Unit> = resultOf {
        reportRepository.sendReport(roleName, topic, description, images)
    }
}
