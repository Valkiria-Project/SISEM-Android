package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.valkiria.uicomponents.model.ui.pill.PillUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PillResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "color") val color: String?
)

fun PillResponse.mapToDomain(): PillUiModel = PillUiModel(
    title = title?.mapToDomain() ?: error("Pill title cannot be null"),
    color = color ?: error("Pill color cannot be null")
)
