package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.body.HeaderResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.banner.finding.FindingsDetailUiModel

@JsonClass(generateAdapter = true)
data class FindingsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<FindingDetailResponse>?
)

fun FindingsDetailResponse.mapToUi(): FindingsDetailUiModel = FindingsDetailUiModel(
    header = header?.mapToUi() ?: error("FindingsDetail header cannot be null"),
    details = details?.map { it.mapToUi() } ?: error("FindingsDetail details cannot be null")
)
