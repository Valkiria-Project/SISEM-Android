package com.skgtecnologia.sisem.domain.auth.model

data class RefreshTokenModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val isAdmin: Boolean
)
