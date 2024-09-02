package com.skgtecnologia.sisem.data.incident.remote.model

import com.squareup.moshi.Json
import com.valkiria.uicomponents.components.incident.model.ResourceUiModel

data class ResourceResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "resource_id") val resourceId: Int,
    @Json(name = "resource") val resource: ResourceDetailResponse
)

fun ResourceResponse.mapToUi(): ResourceUiModel = ResourceUiModel(
    id = id,
    resourceId = resourceId,
    resource = resource.mapToUi()
)
