package com.skgtecnologia.sisem.core.data.remote.model.components.label

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.ListPatientUiModel
import com.valkiria.uicomponents.components.label.TextStyle

@JsonClass(generateAdapter = true)
data class ListPatientResponse(
    @Json(name = "text") val texts: Map<String, String>?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "icon") val icon: String?
)

fun ListPatientResponse.mapToUi(): ListPatientUiModel = ListPatientUiModel(
    texts = texts ?: error("ListText texts cannot be null"),
    textStyle = textStyle ?: error("ListText textStyle cannot be null"),
    icon = icon ?: error("ListText icon cannot be null")
)
