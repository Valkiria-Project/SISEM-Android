package com.skgtecnologia.sisem.data.incident.remote.model

import com.squareup.moshi.Json
import com.valkiria.uicomponents.components.incident.model.PatientUiModel

data class PatientResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "id_aph") val idAph: Int,
    @Json(name = "disabled") val disabled: Boolean
)

fun PatientResponse.mapToUi(): PatientUiModel = PatientUiModel(
    id = id,
    fullName = fullName,
    idAph = idAph,
    disabled = disabled
)
