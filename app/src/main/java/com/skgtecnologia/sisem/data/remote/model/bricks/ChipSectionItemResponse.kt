package com.skgtecnologia.sisem.data.remote.model.bricks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.chip.ChipSectionItemUiModel

@JsonClass(generateAdapter = true)
data class ChipSectionItemResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
)

fun ChipSectionItemResponse.mapToUi(): ChipSectionItemUiModel = ChipSectionItemUiModel(
    id = id ?: error("ChipSectionItem id cannot be null"),
    name = name ?: error("ChipSectionItem name cannot be null")
)
