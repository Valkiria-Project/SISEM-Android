package com.skgtecnologia.sisem.domain.report

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel

interface ReportRepository {

    suspend fun getAddReportRoleScreen(serial: String): ScreenModel

    suspend fun getAddReportScreen(): ScreenModel

    suspend fun sendReport(
        roleName: String?,
        topic: String,
        description: String,
        images: List<ImageModel>
    )
}
