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
    @Json(name = "role") val role: String,
    @Json(name = "preoperational") val preoperational: PreoperationalResponse,
    @Json(name = "turn") val turn: TurnResponse,
    @Json(name = "is_admin") val isAdmin: Boolean
)

fun AccessTokenResponse.mapToDomain(): AccessTokenModel = AccessTokenModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    username = username,
    role = role,
    preoperational = preoperational.mapToDomain(),
    turn = turn.mapToDomain(),
    isAdmin = isAdmin
)
