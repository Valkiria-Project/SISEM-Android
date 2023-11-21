package com.skgtecnologia.sisem.data.incident.remote.model

import com.valkiria.uicomponents.components.incident.model.ResourceUiDetailModel
import com.squareup.moshi.Json

data class ResourceDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String,
    @Json(name = "transit_agency") val transitAgency: String,
    @Json(name = "ic_transit_agency") val icTransitAgency: String
)

fun ResourceDetailResponse.mapToUi(): ResourceUiDetailModel = ResourceUiDetailModel(
    id = id,
    code = code,
    transitAgency = transitAgency,
    icTransitAgency = icTransitAgency
)
