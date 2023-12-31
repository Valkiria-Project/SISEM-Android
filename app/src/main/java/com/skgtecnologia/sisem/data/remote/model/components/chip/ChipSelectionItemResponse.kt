package com.skgtecnologia.sisem.data.remote.model.components.chip

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel

@JsonClass(generateAdapter = true)
data class ChipSelectionItemResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
)

fun ChipSelectionItemResponse.mapToUi(): ChipSelectionItemUiModel = ChipSelectionItemUiModel(
    id = id ?: error("ChipSectionItem id cannot be null"),
    name = name ?: error("ChipSectionItem name cannot be null")
)
