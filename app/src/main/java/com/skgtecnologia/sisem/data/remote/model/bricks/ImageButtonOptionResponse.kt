package com.skgtecnologia.sisem.data.remote.model.bricks

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.body.ImageButtonResponse
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bricks.ImageButtonOptionModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageButtonOptionResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "options") val options: List<ImageButtonResponse>?,
    @Json(name = "margins") val modifier: Modifier?
)

fun ImageButtonOptionResponse.mapToDomain(): ImageButtonOptionModel = ImageButtonOptionModel(
    identifier = identifier ?: error("ImageButtonOption identifier cannot be null"),
    title = title?.mapToDomain() ?: error("ImageButtonOption title cannot be null"),
    options = options?.map { it.mapToDomain() }
        ?: error("ImageButtonOption options cannot be null"),
    modifier = modifier ?: Modifier
)
