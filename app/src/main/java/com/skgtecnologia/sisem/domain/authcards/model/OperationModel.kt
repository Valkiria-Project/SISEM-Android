package com.skgtecnologia.sisem.domain.authcards.model

import com.skgtecnologia.sisem.di.operation.OperationRole

data class OperationModel(
    val preoperationalTime: Long,
    val clinicHistObservationsTime: Long,
    val loginTime: Long,
    val numImgPreoperationalDriver: Int,
    val numImgPreoperationalDoctor: Int,
    val numImgPreoperationalAux: Int,
    val numImgNovelty: Int,
    val authMethod: String,
    val attentionsType: String,
    val status: Boolean,
    val vehicleCode: String?,
    val vehicleConfig: VehicleConfigModel?,
    val operationRole: OperationRole? = null,
    val maxFileSizeKb: String,
    val preoperationalExec: Map<String, Boolean>
)
