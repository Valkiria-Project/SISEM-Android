package com.skgtecnologia.sisem.data.preoperational.remote.model

import com.skgtecnologia.sisem.data.remote.extensions.createRequestBody
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.domain.report.model.ImageModel

fun buildFindingFormDataBody(role: OperationRole, idTurn: String, novelties: List<Novelty>) =
    buildMap {
        put("type", role.name.createRequestBody())
        put("id_preoperational", novelties[0].idPreoperational.toString().createRequestBody())
        put("id_turn", idTurn.createRequestBody())
        put("novelty", novelties[0].novelty.createRequestBody())
    }

fun buildFindingImagesBody(images: List<ImageModel>) = images.associate { imageModel ->
    val file = imageModel.file
    file.name to file.createRequestBody()
}
