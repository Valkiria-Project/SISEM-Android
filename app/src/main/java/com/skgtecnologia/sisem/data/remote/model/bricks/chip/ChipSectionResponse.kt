package com.skgtecnologia.sisem.data.remote.model.bricks.chip

import com.skgtecnologia.sisem.data.remote.model.components.label.ListTextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUI
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel

@JsonClass(generateAdapter = true)
data class ChipSectionResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "list_text") val listText: ListTextResponse?
)

fun ChipSectionResponse.mapToUi(): ChipSectionUiModel = ChipSectionUiModel(
    title = title?.mapToUI() ?: error("ChipSection title cannot be null"),
    listText = listText?.mapToUi() ?: error("ChipSection listText cannot be null")
)
