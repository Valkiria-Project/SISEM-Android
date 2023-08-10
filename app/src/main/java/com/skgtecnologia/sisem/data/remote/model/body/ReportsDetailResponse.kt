package com.skgtecnologia.sisem.data.remote.model.body

import com.skgtecnologia.sisem.data.remote.model.header.HeaderResponse
import com.skgtecnologia.sisem.data.remote.model.header.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.ReportsDetailModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReportsDetailResponse(
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "details") val details: List<DetailResponse>?
)

fun ReportsDetailResponse.mapToDomain(): ReportsDetailModel = ReportsDetailModel(
    header = header?.mapToDomain() ?: error("ReportsDetail header cannot be null"),
    details = details?.map { it.mapToDomain() } ?: error("ReportsDetail details cannot be null")
)