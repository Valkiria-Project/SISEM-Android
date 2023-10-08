package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUI
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipSectionResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "list_text") val listText: ListTextResponse?
)

fun ChipSectionResponse.mapToUi(): ChipSectionUiModel = ChipSectionUiModel(
    title = title?.mapToUI() ?: error("ChipSection title cannot be null"),
    listText = listText?.mapToUi() ?: error("ChipSection listText cannot be null")
)
