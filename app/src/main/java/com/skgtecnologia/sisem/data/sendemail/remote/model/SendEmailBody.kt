package com.skgtecnologia.sisem.data.sendemail.remote.model

import com.squareup.moshi.Json

data class SendEmailBody(
    @Json(name = "id_aph") val idAph: String,
    @Json(name = "to") val to: String,
    @Json(name = "body") val body: String
)
