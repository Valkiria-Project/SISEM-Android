package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.body.HeaderResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.finding.FindingsDetailModel

@JsonClass(generateAdapter = true)
data class FindingsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<FindingDetailResponse>?
)

fun FindingsDetailResponse.mapToDomain(): FindingsDetailModel = FindingsDetailModel(
    header = header?.mapToUi() ?: error("FindingsDetail header cannot be null"),
    details = details?.map { it.mapToDomain() } ?: error("FindingsDetail details cannot be null")
)
