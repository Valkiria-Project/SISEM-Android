package com.skgtecnologia.sisem.data.auth.remote.model

import com.skgtecnologia.sisem.domain.auth.model.RefreshTokenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class RefreshTokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expiresIn: Int
)

fun RefreshTokenResponse.mapToDomain(): RefreshTokenModel = RefreshTokenModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    expDate = expiresIn.mapToDomain()
)

fun Int.mapToDomain(): LocalDateTime = LocalDateTime.now().plusSeconds(this.toLong())
