package com.skgtecnologia.sisem.data.incident.remote.model

import com.squareup.moshi.Json
import com.valkiria.uicomponents.components.incident.model.ResourceDetailUiModel

data class ResourceDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "ic_transit_agency") val icTransitAgency: String
)

fun ResourceDetailResponse.mapToUi(): ResourceDetailUiModel = ResourceDetailUiModel(
    id = id,
    code = code,
    name = name,
    icTransitAgency = icTransitAgency
)
