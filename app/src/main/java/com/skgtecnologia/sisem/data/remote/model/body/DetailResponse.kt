package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.header.TextResponse
import com.skgtecnologia.sisem.data.remote.model.header.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.DetailModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailResponse(
    @Json(name = "images") val images: List<String>?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "subtitle") val subtitle: TextResponse?,
    @Json(name = "hour") val description: TextResponse?,
    @Json(name = "margins") val modifier: Modifier?
)

fun DetailResponse.mapToDomain(): DetailModel = DetailModel(
    images = images ?: error("Detail images cannot be null"),
    title = title?.mapToDomain() ?: error("Detail title cannot be null"),
    subtitle = subtitle?.mapToDomain() ?: error("Detail subtitle cannot be null"),
    description = description?.mapToDomain() ?: error("Detail description cannot be null"),
    modifier = modifier ?: error("Detail modifier cannot be null")
)