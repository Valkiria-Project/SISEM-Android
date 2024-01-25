package com.skgtecnologia.sisem.data.deviceauth.remote.model

import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeviceResponse(
    @Json(name = "message") val message: String
)

fun DeviceResponse.mapToDomain(): DeviceModel = DeviceModel(
    message = message
)
