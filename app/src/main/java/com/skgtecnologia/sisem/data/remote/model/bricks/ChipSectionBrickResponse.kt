package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.header.TextResponse
import com.skgtecnologia.sisem.data.remote.model.header.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.ChipSectionModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipSectionBrickResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "list_text") val listText: ListTextBrickResponse?
)

fun ChipSectionBrickResponse.mapToDomain(): ChipSectionModel = ChipSectionModel(
    title = title?.mapToDomain() ?: error("ChipSection title cannot be null"),
    listText = listText?.mapToDomain() ?: error("ChipSection listText cannot be null")
)
