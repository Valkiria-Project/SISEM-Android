package com.skgtecnologia.sisem.data.auth.remote.model

import com.skgtecnologia.sisem.domain.auth.model.RefreshTokenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenResponse(
    @Json(name = "token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "type") val tokenType: String,
    @Json(name = "is_admin") val isAdmin: Boolean
)

fun RefreshTokenResponse.mapToDomain(): RefreshTokenModel = RefreshTokenModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    isAdmin = isAdmin
)
