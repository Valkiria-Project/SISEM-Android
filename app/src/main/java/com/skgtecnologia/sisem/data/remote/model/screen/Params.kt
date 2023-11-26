package com.skgtecnologia.sisem.data.remote.model.screen

import com.squareup.moshi.Json

data class Params(
    @Json(name = "serial") val serial: String? = null,
    @Json(name = "code") val code: String? = null,
    @Json(name = "turn_id") val turnId: String? = null,
    @Json(name = "id_aph") val idAph: String? = null,
    @Json(name = "document") val document: String? = null
)
