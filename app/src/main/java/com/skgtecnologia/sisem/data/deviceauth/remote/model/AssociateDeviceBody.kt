package com.skgtecnologia.sisem.data.deviceauth.remote.model

import com.squareup.moshi.Json

data class AssociateDeviceBody(
    @Json(name = "license_plate") val licensePlate: String,
    @Json(name = "serial") val serial: String,
    @Json(name = "code") val code: String
)
