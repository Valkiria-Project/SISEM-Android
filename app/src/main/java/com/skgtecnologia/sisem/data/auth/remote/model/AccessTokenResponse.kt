package com.skgtecnologia.sisem.data.auth.remote.model

import com.skgtecnologia.sisem.data.remote.model.banner.BannerResponse
import com.skgtecnologia.sisem.data.remote.model.banner.mapToDomain
import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(
    @Json(name = "token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "type") val tokenType: String,
    @Json(name = "username") val username: String,
    @Json(name = "role") val role: String,
    @Json(name = "preoperational") val preoperational: PreOperationalResponse?,
    @Json(name = "turn") val turn: TurnResponse?,
    @Json(name = "is_admin") val isAdmin: Boolean,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "name_user") val nameUser: String,
    @Json(name = "warning") val warning: BannerResponse?,
    @Json(name = "doc_type") val docType: String,
    @Json(name = "document") val document: String
)

fun AccessTokenResponse.mapToDomain(): AccessTokenModel = AccessTokenModel(
    dateTime = LocalDateTime.now(),
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    username = username,
    role = role,
    preoperational = preoperational?.mapToDomain(),
    turn = turn?.mapToDomain(),
    isAdmin = isAdmin,
    userId = userId,
    nameUser = nameUser,
    warning = warning?.mapToDomain(),
    docType = docType,
    document = document
)
