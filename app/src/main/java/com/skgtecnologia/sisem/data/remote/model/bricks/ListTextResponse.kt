package com.skgtecnologia.sisem.data.remote.model.bricks

import com.valkiria.uicomponents.model.props.ListTextModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.props.TextStyle

@JsonClass(generateAdapter = true)
data class ListTextResponse(
    @Json(name = "text") val texts: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?
)

fun ListTextResponse.mapToUi(): ListTextModel = ListTextModel(
    texts = texts ?: error("ListText texts cannot be null"),
    textStyle = textStyle ?: error("ListText textStyle cannot be null")
)
