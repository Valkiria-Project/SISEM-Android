package com.skgtecnologia.sisem.core.data.remote.model.bricks.banner.report

import com.skgtecnologia.sisem.core.data.remote.model.screen.HeaderResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel

@JsonClass(generateAdapter = true)
data class ReportsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<ReportDetailResponse>?
)

fun ReportsDetailResponse.mapToUi(): ReportsDetailUiModel = ReportsDetailUiModel(
    header = header?.mapToUi() ?: error("ReportsDetail header cannot be null"),
    details = details?.map { it.mapToUi() } ?: error("ReportsDetail details cannot be null")
)
