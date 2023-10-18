package com.skgtecnologia.sisem.data.report.remote.model

import com.skgtecnologia.sisem.data.remote.extensions.createRequestBody
import com.skgtecnologia.sisem.domain.report.model.ImageModel

fun buildReportFormDataBody(topic: String, description: String) = buildMap {
    put("subject", topic.createRequestBody())
    put("description", description.createRequestBody())
}

fun buildReportImagesBody(images: List<ImageModel>) = images.associate { imageModel ->
    val file = imageModel.file
    file.name to file.createRequestBody()
}
