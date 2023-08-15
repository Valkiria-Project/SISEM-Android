package com.skgtecnologia.sisem.data.changepassword.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordResponse(
    @Json(name = "message") val message: String
)
