package com.skgtecnologia.sisem.data.preoperational.remote.model

import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.squareup.moshi.Json

data class NoveltyBody(
    @Json(name = "id_preoperational") val idPreoperational: String,
    @Json(name = "novelty") val novelty: String
)

fun Novelty.mapToBody() = NoveltyBody(
    idPreoperational = idPreoperational,
    novelty = novelty
)
