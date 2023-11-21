package com.skgtecnologia.sisem.data.incident.remote.model

import com.skgtecnologia.sisem.domain.incident.model.ResourceModel
import com.squareup.moshi.Json

data class ResourceResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "resource_id") val resourceId: Int,
    @Json(name = "resource") val resource: ResourceDetailResponse
)

fun ResourceResponse.mapToDomain(): ResourceModel = ResourceModel(
    id = id,
    resourceId = resourceId,
    resource = resource.mapToDomain()
)
