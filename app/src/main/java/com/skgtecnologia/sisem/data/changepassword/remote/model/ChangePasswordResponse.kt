package com.skgtecnologia.sisem.data.changepassword.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordResponse(
    val message: String
)
