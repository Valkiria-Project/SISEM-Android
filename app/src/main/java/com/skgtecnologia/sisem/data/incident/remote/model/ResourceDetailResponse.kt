package com.skgtecnologia.sisem.data.incident.remote.model

import com.skgtecnologia.sisem.domain.incident.model.ResourceDetailModel
import com.squareup.moshi.Json

data class ResourceDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String,
    @Json(name = "transit_agency") val transitAgency: String,
    @Json(name = "ic_transit_agency") val icTransitAgency: String
)

fun ResourceDetailResponse.mapToDomain(): ResourceDetailModel = ResourceDetailModel(
    id = id,
    code = code,
    transitAgency = transitAgency,
    icTransitAgency = icTransitAgency
)
