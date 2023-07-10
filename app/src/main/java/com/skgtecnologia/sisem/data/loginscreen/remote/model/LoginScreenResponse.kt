package com.skgtecnologia.sisem.data.loginscreen.remote.model

import com.skgtecnologia.sisem.data.myscreen.remote.model.BodyRowResponse
import com.skgtecnologia.sisem.domain.loginscreen.model.LoginScreenModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginScreenResponse(
    val body: List<BodyRowResponse>? = null
)

fun LoginScreenResponse.mapToDomain(): LoginScreenModel = LoginScreenModel(
    body = body?.map { it.mapToDomain() } ?: error("body cannot be null")
)
