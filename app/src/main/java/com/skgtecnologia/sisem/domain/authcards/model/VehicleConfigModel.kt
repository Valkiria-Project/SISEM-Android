package com.skgtecnologia.sisem.domain.authcards.model

data class VehicleConfigModel(
    val zone: String,
    val statusCode: String,
    val provider: String,
    val plate: String,
    val preoperational: String,
    val typeResource: String,
    val statusColor: String
)
