package com.skgtecnologia.sisem.data.auth.remote.model

import com.squareup.moshi.Json

data class RefreshBody(
    @Json(name = "refresh_token") val refreshToken: String
)
