package com.skgtecnologia.sisem.domain.auth.model

data class AccessTokenModel(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val username: String,
    val role: String,
    val isAdmin: Boolean,
    val nameUser: String,
    val preoperational: PreOperationalModel?,
    val turn: TurnModel?
)
