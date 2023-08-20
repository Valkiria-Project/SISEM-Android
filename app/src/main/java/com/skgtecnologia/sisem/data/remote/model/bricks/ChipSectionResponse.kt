package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bricks.ChipSectionModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipSectionResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "list_text") val listText: ListTextResponse?
)

fun ChipSectionResponse.mapToDomain(): ChipSectionModel = ChipSectionModel(
    title = title?.mapToDomain() ?: error("ChipSection title cannot be null"),
    listText = listText?.mapToDomain() ?: error("ChipSection listText cannot be null")
)
