package com.skgtecnologia.sisem.data.remote.model.bricks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel

@JsonClass(generateAdapter = true)
data class ChipOptionResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "selected") val selected: Boolean?
)

fun ChipOptionResponse.mapToUi(): ChipOptionUiModel = ChipOptionUiModel(
    id = id ?: error("ChipOption id cannot be null"),
    name = name ?: error("ChipOption name cannot be null"),
    selected = selected ?: false
)
