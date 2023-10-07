package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.body.HeaderResponse
import com.valkiria.uicomponents.model.ui.report.ReportsDetailModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<ReportDetailResponse>?
)

fun ReportsDetailResponse.mapToDomain(): ReportsDetailModel = ReportsDetailModel(
    header = header?.mapToUi() ?: error("ReportsDetail header cannot be null"),
    details = details?.map { it.mapToDomain() } ?: error("ReportsDetail details cannot be null")
)
