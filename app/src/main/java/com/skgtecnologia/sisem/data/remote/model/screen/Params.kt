package com.skgtecnologia.sisem.data.remote.model.screen

import com.squareup.moshi.Json

data class Params(
    @Json(name = "serial") val serial: String? = null,
    @Json(name = "code") val code: String? = null,
    @Json(name = "turn_id") val turnId: String? = null,
    @Json(name = "incident_code") val incidentCode: String? = null,
    @Json(name = "patient_id") val patientId: String? = null
)
