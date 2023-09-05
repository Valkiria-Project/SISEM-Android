package com.skgtecnologia.sisem.data.remote.model.props

import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.props.TextStyle

@JsonClass(generateAdapter = true)
data class TextResponse(
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
)

fun TextResponse.mapToDomain(): TextModel = TextModel(
    text = text ?: error("TextModel text cannot be null"),
    textStyle = textStyle ?: error("TextModel textStyle cannot be null")
)
