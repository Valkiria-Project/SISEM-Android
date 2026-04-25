package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bricks.PillModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PillResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "color") val color: String?
)

fun PillResponse.mapToDomain(): PillModel = PillModel(
    title = title?.mapToDomain() ?: error("Pill title cannot be null"),
    color = color ?: error("Pill color cannot be null")
)
