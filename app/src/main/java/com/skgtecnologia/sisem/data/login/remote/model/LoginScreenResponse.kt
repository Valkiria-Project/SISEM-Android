package com.skgtecnologia.sisem.data.login.remote.model

import com.skgtecnologia.sisem.data.myscreen.remote.model.BodyRowResponse
import com.skgtecnologia.sisem.domain.login.model.LoginScreenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginScreenResponse(
    @Json(name = "body") val body: List<BodyRowResponse>? = null
)

fun LoginScreenResponse.mapToDomain(): LoginScreenModel = LoginScreenModel(
    body = body?.map { it.mapToDomain() } ?: error("body cannot be null")
)
