package com.skgtecnologia.sisem.data.remote.model.components.dropdown

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.dropdown.DropDownItemUiModel

@JsonClass(generateAdapter = true)
data class DropDownItemResponse(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?
)

fun DropDownItemResponse.mapToUi() = DropDownItemUiModel(
    id = id ?: error("DropDownItem id cannot be null"),
    name = name ?: error("DropDownItem name cannot be null")
)
