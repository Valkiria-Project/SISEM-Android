package com.skgtecnologia.sisem.domain.auth.model

import java.time.LocalDateTime

data class RefreshTokenModel(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expDate: LocalDateTime,
    val isAdmin: Boolean? = null
)
