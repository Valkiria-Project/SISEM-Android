package com.skgtecnologia.sisem.data.incident.remote.model

import com.skgtecnologia.sisem.domain.incident.model.PatientModel
import com.squareup.moshi.Json

data class PatientResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "id_aph") val idAph: Int
)

fun PatientResponse.mapToDomain(): PatientModel = PatientModel(
    id = id,
    fullName = fullName,
    idAph = idAph
)
