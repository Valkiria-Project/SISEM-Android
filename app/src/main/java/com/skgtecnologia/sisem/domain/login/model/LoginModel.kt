package com.skgtecnologia.sisem.domain.login.model

data class LoginModel(
    val accessToken: String,
    val refreshToken: String?,
    val tokenType: String,
    val username: String,
    val role: String
)
