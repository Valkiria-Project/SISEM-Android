package com.skgtecnologia.sisem.data.remote.model.bricks

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.dropdown.DropDownItemUiModel

@JsonClass(generateAdapter = true)
data class DropDownItemResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?,
)

fun DropDownItemResponse.mapToUi(): DropDownItemUiModel = DropDownItemUiModel(
    id = id ?: error("DropDownItem id cannot be null"),
    name = name ?: error("DropDownItem name cannot be null")
)
