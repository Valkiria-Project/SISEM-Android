package com.skgtecnologia.sisem.data.deviceauth.remote.model

import com.skgtecnologia.sisem.domain.deviceauth.model.AssociateDeviceModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssociateDeviceResponse(
    @Json(name = "message") val message: String
)

fun AssociateDeviceResponse.mapToDomain(): AssociateDeviceModel = AssociateDeviceModel(
    message = message
)
