package com.skgtecnologia.sisem.data.auth.remote.model

import com.squareup.moshi.Json

data class AuthenticateBody(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
    @Json(name = "code") val code: String,
    @Json(name = "id_turn") val idTurn: String?,
    @Json(name = "force_close_session") val forceCloseSession: Boolean = false
)
