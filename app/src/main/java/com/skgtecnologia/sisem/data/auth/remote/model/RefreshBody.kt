package com.skgtecnologia.sisem.data.auth.remote.model

import com.squareup.moshi.Json

data class RefreshBody(
    @Json(name = "token") val refreshToken: String
)
