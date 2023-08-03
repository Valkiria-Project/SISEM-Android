package com.skgtecnologia.sisem.domain.login.model

data class AccessTokenModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val username: String,
    val role: String,
    val preoperational: PreOperationalModel,
    val turn: TurnModel,
    val isAdmin: Boolean
)
