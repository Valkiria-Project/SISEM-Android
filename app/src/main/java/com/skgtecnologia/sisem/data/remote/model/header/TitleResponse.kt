package com.skgtecnologia.sisem.data.remote.model.header

import com.skgtecnologia.sisem.domain.model.header.Title
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class TitleResponse(
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
)

fun TitleResponse.mapToDomain(): Title = Title(
    text = this.text ?: error("Title text cannot be null"),
    textStyle = this.textStyle ?: error("Title textStyle cannot be null")
)
