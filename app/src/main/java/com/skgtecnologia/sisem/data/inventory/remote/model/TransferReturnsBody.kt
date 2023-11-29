package com.skgtecnologia.sisem.data.inventory.remote.model

import com.squareup.moshi.Json

data class TransferReturnsBody(
    @Json(name = "id_turn") val idTurn: String,
    @Json(name = "fields_values") val fieldsValues: Map<String, String>
)
