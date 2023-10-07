package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.body.HeaderResponse
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<ReportDetailResponse>?
)

fun ReportsDetailResponse.mapToUi(): ReportsDetailUiModel = ReportsDetailUiModel(
    header = header?.mapToUi() ?: error("ReportsDetail header cannot be null"),
    details = details?.map { it.mapToUi() } ?: error("ReportsDetail details cannot be null")
)
