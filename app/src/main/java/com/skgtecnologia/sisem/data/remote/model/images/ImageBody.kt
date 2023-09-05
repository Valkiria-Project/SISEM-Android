package com.skgtecnologia.sisem.data.remote.model.images

import com.squareup.moshi.Json

data class ImageBody(
    @Json(name = "file_name") val fileName: String,
    @Json(name = "file") val file: String
)
