package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.header.TextResponse
import com.skgtecnologia.sisem.data.remote.model.header.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.PillModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PillBrickResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "color") val color: String?
)

fun PillBrickResponse.mapToDomain(): PillModel = PillModel(
    title = title?.mapToDomain() ?: error("Pill title cannot be null"),
    color = color ?: error("Pill color cannot be null")
)
