package com.skgtecnologia.sisem.data.remote.model.header

import com.skgtecnologia.sisem.domain.model.header.Subtitle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class SubtitleResponse(
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?
)

fun SubtitleResponse.mapToDomain(): Subtitle = Subtitle(
    text = this.text ?: error("text cannot be null"),
    textStyle = this.textStyle ?: error("textStyle cannot be null")
)
