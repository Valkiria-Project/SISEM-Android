package com.skgtecnologia.sisem.domain.auth.model

import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import java.time.LocalDateTime

data class AccessTokenModel(
    val userId: Int,
    val dateTime: LocalDateTime,
    val accessToken: String,
    val refreshToken: String,
    val refreshDateTime: LocalDateTime,
    val tokenType: String,
    val username: String,
    val role: String,
    val isAdmin: Boolean,
    val nameUser: String,
    val preoperational: PreOperationalModel?,
    val configPreoperational: Boolean = false,
    val turn: TurnModel?,
    val warning: BannerModel? = null,
    val isWarning: Boolean,
    val docType: String,
    val document: String,
    val expDate: LocalDateTime
)
