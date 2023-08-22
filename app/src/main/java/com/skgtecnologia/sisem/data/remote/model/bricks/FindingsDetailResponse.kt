package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.header.HeaderResponse
import com.skgtecnologia.sisem.data.remote.model.header.mapToDomain
import com.skgtecnologia.sisem.domain.model.bricks.FindingsDetailModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FindingsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<FindingDetailResponse>?
)

fun FindingsDetailResponse.mapToDomain(): FindingsDetailModel = FindingsDetailModel(
    header = header?.mapToDomain() ?: error("FindingsDetail header cannot be null"),
    details = details?.map { it.mapToDomain() } ?: error("FindingsDetail details cannot be null")
)
