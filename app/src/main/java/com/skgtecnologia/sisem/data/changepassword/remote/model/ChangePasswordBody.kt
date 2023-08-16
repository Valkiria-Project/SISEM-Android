package com.skgtecnologia.sisem.data.changepassword.remote.model

import com.squareup.moshi.Json

data class ChangePasswordBody(
    @Json(name = "old_password") val oldPassword: String,
    @Json(name = "new_password") val newPassword: String
)
