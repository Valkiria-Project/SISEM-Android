package com.skgtecnologia.sisem.data.remote.model.components.card

import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.card.PillUiModel

@JsonClass(generateAdapter = true)
data class PillResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "color") val color: String?
)

fun PillResponse.mapToUi(): PillUiModel = PillUiModel(
    title = title?.mapToUi() ?: error("Pill title cannot be null"),
    color = color ?: error("Pill color cannot be null")
)
