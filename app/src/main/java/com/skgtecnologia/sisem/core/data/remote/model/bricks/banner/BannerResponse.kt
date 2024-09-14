package com.skgtecnologia.sisem.core.data.remote.model.bricks.banner

import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.bricks.banner.DEFAULT_ICON_COLOR

@JsonClass(generateAdapter = true)
data class BannerResponse(
    @Json(name = "icon") val icon: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null
)

fun BannerResponse.mapToDomain() = BannerModel(
    icon = icon ?: error("Banner icon cannot be null"),
    iconColor = DEFAULT_ICON_COLOR,
    title = title ?: error("Banner title cannot be null"),
    description = description ?: error("Banner description cannot be null")
)
