package com.skgtecnologia.sisem.data.remote.model.screen

import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.footer.FooterResponse
import com.skgtecnologia.sisem.data.remote.model.components.footer.mapToUi
import com.skgtecnologia.sisem.data.remote.model.components.header.HeaderResponse
import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScreenResponse(
    @Json(name = "header") val header: HeaderResponse? = null,
    @Json(name = "body") val body: List<BodyRowResponse>? = null,
    @Json(name = "footer") val footer: FooterResponse? = null
)

fun ScreenResponse.mapToDomain(): ScreenModel = ScreenModel(
    header = header?.mapToUi(),
    body = body?.map { it.mapToUi() } ?: error("ScreenModel body cannot be null"),
    footer = footer?.mapToUi()
)
