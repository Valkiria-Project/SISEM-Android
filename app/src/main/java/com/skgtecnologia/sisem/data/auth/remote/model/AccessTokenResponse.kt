package com.skgtecnologia.sisem.data.auth.remote.model

import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(
    @Json(name = "token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "type") val tokenType: String,
    @Json(name = "user_name") val username: String,
    @Json(name = "role") val role: String
)

fun AccessTokenResponse.mapToDomain(): AccessTokenModel = AccessTokenModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    username = username,
    role = role
)
