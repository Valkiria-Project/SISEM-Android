package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUI
import com.valkiria.uicomponents.components.card.PillUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PillResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "color") val color: String?
)

fun PillResponse.mapToUi(): PillUiModel = PillUiModel(
    title = title?.mapToUI() ?: error("Pill title cannot be null"),
    color = color ?: error("Pill color cannot be null")
)
