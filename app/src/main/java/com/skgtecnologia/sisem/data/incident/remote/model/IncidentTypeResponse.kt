package com.skgtecnologia.sisem.data.incident.remote.model

import com.skgtecnologia.sisem.domain.incident.model.IncidentTypeModel
import com.squareup.moshi.Json

data class IncidentTypeResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String
)

fun IncidentTypeResponse.mapToDomain(): IncidentTypeModel = IncidentTypeModel(
    id = id,
    code = code
)
