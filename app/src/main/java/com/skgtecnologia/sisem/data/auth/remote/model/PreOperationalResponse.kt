package com.skgtecnologia.sisem.data.auth.remote.model

import com.skgtecnologia.sisem.domain.login.model.PreOperationalModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PreOperationalResponse(
    @Json(name = "preoperational") val preoperational: String,
    @Json(name = "type_resource") val typeResource: String,
    @Json(name = "status") val status: Boolean,
)

fun PreOperationalResponse.mapToDomain(): PreOperationalModel = PreOperationalModel(
    preoperational = preoperational,
    typeResource = typeResource,
    status = status
)
