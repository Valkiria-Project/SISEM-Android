package com.skgtecnologia.sisem.data.auth.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogoutResponse(
    @Json(name = "message") val message: String
)
