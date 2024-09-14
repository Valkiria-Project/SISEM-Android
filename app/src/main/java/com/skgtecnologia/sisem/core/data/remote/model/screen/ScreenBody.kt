package com.skgtecnologia.sisem.core.data.remote.model.screen

import com.squareup.moshi.Json

data class ScreenBody(
    @Json(name = "params") val params: Params
)
