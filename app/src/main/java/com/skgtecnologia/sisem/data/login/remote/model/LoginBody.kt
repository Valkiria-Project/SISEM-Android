package com.skgtecnologia.sisem.data.login.remote.model

import com.squareup.moshi.Json

data class LoginBody(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String
)
