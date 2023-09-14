package com.skgtecnologia.sisem.data.remote.model.props

import com.squareup.moshi.Json

data class MarginsResponse(
    @Json(name = "top") val top: Int?,
    @Json(name = "bottom") val bottom: Int?,
    @Json(name = "left") val left: Int?,
    @Json(name = "right") val right: Int?
)
