package com.skgtecnologia.sisem.data.login.remote.model

import com.skgtecnologia.sisem.data.remote.model.body.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.footer.FooterResponse
import com.skgtecnologia.sisem.data.remote.model.footer.mapToDomain
import com.skgtecnologia.sisem.data.remote.model.header.HeaderResponse
import com.skgtecnologia.sisem.data.remote.model.header.mapToDomain
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "header") val header: HeaderResponse? = null,
    @Json(name = "body") val body: List<BodyRowResponse>? = null,
    @Json(name = "footer") val footer: FooterResponse? = null
)

fun LoginResponse.mapToDomain(): String = "JGARCíA"
