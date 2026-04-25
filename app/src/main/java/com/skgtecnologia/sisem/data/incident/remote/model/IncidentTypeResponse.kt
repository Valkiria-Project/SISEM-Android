package com.skgtecnologia.sisem.data.incident.remote.model

import com.squareup.moshi.Json
import com.valkiria.uicomponents.components.incident.model.IncidentTypeUiModel

data class IncidentTypeResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String
)

fun IncidentTypeResponse.mapToUi(): IncidentTypeUiModel = IncidentTypeUiModel(
    id = id,
    code = code
)
