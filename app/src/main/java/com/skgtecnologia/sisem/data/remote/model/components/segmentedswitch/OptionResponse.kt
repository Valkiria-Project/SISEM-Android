package com.skgtecnologia.sisem.data.remote.model.components.segmentedswitch

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.segmentedswitch.OptionUiModel

@JsonClass(generateAdapter = true)
data class OptionResponse(
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "color") val color: String?
)

fun OptionResponse.mapToUi(): OptionUiModel = OptionUiModel(
    text = text ?: error("Option text cannot be null"),
    textStyle = textStyle ?: error("Option textStyle cannot be null"),
    color = color ?: error("Option color cannot be null")
)
