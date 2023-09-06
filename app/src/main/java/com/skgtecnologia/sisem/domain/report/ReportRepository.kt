package com.skgtecnologia.sisem.domain.report

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.skgtecnologia.sisem.domain.report.model.ImageModel

interface ReportRepository {

    suspend fun getAddReportScreen(serial: String): ScreenModel

    suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>
    )
}
