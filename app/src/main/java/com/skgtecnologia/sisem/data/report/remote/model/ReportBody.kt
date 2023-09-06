package com.skgtecnologia.sisem.data.report.remote.model

import com.skgtecnologia.sisem.data.remote.model.images.ImageBody
import com.squareup.moshi.Json

data class ReportBody(
    @Json(name = "subject") val topic: String,
    @Json(name = "description") val description: String,
    @Json(name = "id_turn") val idTurn: Int,
    @Json(name = "images") val images: List<ImageBody>
)
