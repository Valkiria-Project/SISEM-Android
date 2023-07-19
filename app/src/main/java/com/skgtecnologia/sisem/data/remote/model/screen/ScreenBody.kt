package com.skgtecnologia.sisem.data.remote.model.screen

import com.squareup.moshi.Json

data class ScreenBody(
    @Json(name = "params") val params: Params
)
