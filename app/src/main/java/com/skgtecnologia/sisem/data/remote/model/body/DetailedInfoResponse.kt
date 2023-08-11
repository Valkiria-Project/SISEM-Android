package com.skgtecnologia.sisem.data.remote.model.body

import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailedInfoResponse(
    @Json(name = "label") val label: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "text") val text: String?
)

fun DetailedInfoResponse.mapToUi(): DetailedInfoUiModel = DetailedInfoUiModel(
    label = label ?: error("Detailed info label cannot be null"),
    icon = icon ?: error("Detailed info icon cannot be null"),
    text = text ?: error("Detailed info text cannot be null")
)
