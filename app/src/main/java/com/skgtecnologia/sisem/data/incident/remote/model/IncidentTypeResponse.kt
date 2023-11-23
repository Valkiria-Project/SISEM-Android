package com.skgtecnologia.sisem.data.incident.remote.model

import com.valkiria.uicomponents.components.incident.model.IncidentUiTypeModel
import com.squareup.moshi.Json

data class IncidentTypeResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "code") val code: String
)

fun IncidentTypeResponse.mapToUi(): IncidentUiTypeModel = IncidentUiTypeModel(
    id = id,
    code = code
)
