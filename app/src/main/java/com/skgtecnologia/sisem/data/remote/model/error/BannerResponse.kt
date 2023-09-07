package com.skgtecnologia.sisem.data.remote.model.error

import com.skgtecnologia.sisem.domain.model.error.BannerModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.banner.DEFAULT_ICON_COLOR

@JsonClass(generateAdapter = true)
data class BannerResponse(
    @Json(name = "icon") val icon: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null
)

fun BannerResponse.mapToDomain() = BannerModel(
    icon = icon ?: error("Error icon cannot be null"),
    iconColor = DEFAULT_ICON_COLOR,
    title = title ?: error("Error title cannot be null"),
    description = description ?: error("Error description cannot be null")
)
