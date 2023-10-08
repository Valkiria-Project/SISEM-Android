package com.skgtecnologia.sisem.data.remote.model.components.label

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel

@JsonClass(generateAdapter = true)
data class TextResponse(
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
)

fun TextResponse.mapToUI(alternativeText: String? = null): TextUiModel = TextUiModel(
    text = alternativeText ?: text ?: error("TextModel text cannot be null"),
    textStyle = textStyle ?: error("TextModel textStyle cannot be null")
)
