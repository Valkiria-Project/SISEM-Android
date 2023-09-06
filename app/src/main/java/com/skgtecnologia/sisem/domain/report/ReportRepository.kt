package com.skgtecnologia.sisem.domain.report

import com.skgtecnologia.sisem.domain.report.model.ImageModel

interface ReportRepository {

    suspend fun sendReport(
        topic: String,
        description: String,
        images: List<ImageModel>
    )
}
