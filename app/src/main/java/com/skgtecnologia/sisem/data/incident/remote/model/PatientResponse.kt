package com.skgtecnologia.sisem.data.incident.remote.model

import com.valkiria.uicomponents.components.incident.model.PatientUiModel
import com.squareup.moshi.Json

data class PatientResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "id_aph") val idAph: Int
)

fun PatientResponse.mapToUi(): PatientUiModel = PatientUiModel(
    id = id,
    fullName = fullName,
    idAph = idAph
)
