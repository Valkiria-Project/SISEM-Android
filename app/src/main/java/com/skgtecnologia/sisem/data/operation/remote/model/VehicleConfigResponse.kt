package com.skgtecnologia.sisem.data.operation.remote.model

import com.skgtecnologia.sisem.domain.authcards.model.VehicleConfigModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleConfigResponse(
    @Json(name = "zone") val zone: String?,
    @Json(name = "status_code") val statusCode: String?,
    @Json(name = "provider") val provider: String?,
    @Json(name = "plate") val plate: String?,
    @Json(name = "preoperational") val preoperational: String?,
    @Json(name = "type_resource") val typeResource: String?,
    @Json(name = "status_color") val statusColor: String?
)

fun VehicleConfigResponse.mapToDomain(): VehicleConfigModel = VehicleConfigModel(
    zone = zone ?: error("VehicleConfig zone cannot be null"),
    statusCode = statusCode ?: error("VehicleConfig statusCode cannot be null"),
    provider = provider ?: error("VehicleConfig provider cannot be null"),
    plate = plate ?: error("VehicleConfig plate cannot be null"),
    preoperational = preoperational ?: error("VehicleConfig preoperational cannot be null"),
    typeResource = typeResource ?: error("VehicleConfig typeResource cannot be null"),
    statusColor = statusColor ?: error("VehicleConfig statusColor cannot be null")
)
