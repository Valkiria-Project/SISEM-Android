package com.skgtecnologia.sisem.domain.auth.model

import com.skgtecnologia.sisem.domain.model.error.ErrorModel
import java.time.LocalDateTime

data class AccessTokenModel(
    val userId: Int,
    val dateTime: LocalDateTime,
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val username: String,
    val role: String,
    val isAdmin: Boolean,
    val nameUser: String,
    val preoperational: PreOperationalModel?,
    val turn: TurnModel?,
    val warning: ErrorModel? = null,
    val docType: String,
    val document: String
)
