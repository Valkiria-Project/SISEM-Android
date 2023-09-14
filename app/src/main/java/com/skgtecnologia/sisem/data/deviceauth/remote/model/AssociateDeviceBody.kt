package com.skgtecnologia.sisem.data.deviceauth.remote.model

import com.squareup.moshi.Json

data class AssociateDeviceBody(
    @Json(name = "serial") val serial: String,
    @Json(name = "code") val code: String,
    @Json(name = "disassociate_device") val disassociateDevice: Boolean
)
