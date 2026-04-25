package com.skgtecnologia.sisem.data.remote.model.components.label

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextStyle

@JsonClass(generateAdapter = true)
data class ListTextResponse(
    @Json(name = "text") val texts: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?
)

fun ListTextResponse.mapToUi(): ListTextUiModel = ListTextUiModel(
    texts = texts ?: error("ListText texts cannot be null"),
    textStyle = textStyle ?: error("ListText textStyle cannot be null")
)
