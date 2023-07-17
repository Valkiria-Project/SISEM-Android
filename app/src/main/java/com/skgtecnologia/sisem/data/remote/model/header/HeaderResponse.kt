package com.skgtecnologia.sisem.data.remote.model.header

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HeaderResponse(
    @Json(name = "title") val title: TitleResponse?,
    @Json(name = "subtitle") val subtitle: SubtitleResponse?,
    @Json(name = "left_icon") val leftIcon: String?,
    @Json(name = "margins") val modifier: Modifier?
)

fun HeaderResponse.mapToDomain(): HeaderModel = HeaderModel(
    title = this.title?.mapToDomain() ?: error("Header title cannot be null"),
    subtitle = this.subtitle?.mapToDomain(),
    leftIcon = this.leftIcon,
    modifier = this.modifier ?: Modifier
)
