package com.skgtecnologia.sisem.data.preoperational.remote.model

import com.skgtecnologia.sisem.data.remote.model.images.ImageBody
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.squareup.moshi.Json

data class NoveltyBody(
    @Json(name = "id_preoperational") val idPreoperational: Int,
    @Json(name = "novelty") val novelty: String,
    @Json(name = "images") val images: List<ImageBody>
)

fun Novelty.mapToBody() = NoveltyBody(
    idPreoperational = idPreoperational,
    novelty = novelty,
    images = images
)
