package com.skgtecnologia.sisem.domain.preoperational.model

import com.skgtecnologia.sisem.data.remote.model.images.ImageBody

data class Novelty(
    val idPreoperational: Int,
    val novelty: String,
    val images: List<ImageBody>
)
