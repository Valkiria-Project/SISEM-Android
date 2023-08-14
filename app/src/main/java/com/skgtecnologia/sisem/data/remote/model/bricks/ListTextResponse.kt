package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.domain.model.bricks.ListTextModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class ListTextBrickResponse(
    @Json(name = "text") val texts: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?
)

fun ListTextBrickResponse.mapToDomain(): ListTextModel = ListTextModel(
    texts = texts ?: error("ListText texts cannot be null"),
    textStyle = textStyle ?: error("ListText textStyle cannot be null")
)
