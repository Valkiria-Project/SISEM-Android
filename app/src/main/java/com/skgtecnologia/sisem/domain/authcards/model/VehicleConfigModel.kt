package com.skgtecnologia.sisem.domain.authcards.model

data class VehicleConfigModel(
    val zone: String,
    val statusCode: String,
    val provider: String,
    val plate: String,
    val preoperational: String, // Use this in order to decide if PreOp is not filled
    val typeResource: String,
    val statusColor: String
)
