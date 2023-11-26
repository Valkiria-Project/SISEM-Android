package com.skgtecnologia.sisem.data.signature.remote.model

import com.squareup.moshi.Json

data class SignatureBody(
    @Json(name = "document") val document: String,
    @Json(name = "signature") val signature: String
)
