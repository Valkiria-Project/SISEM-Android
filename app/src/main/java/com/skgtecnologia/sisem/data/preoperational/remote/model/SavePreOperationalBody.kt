package com.skgtecnologia.sisem.data.preoperational.remote.model

import com.squareup.moshi.Json

data class SavePreOperationalBody(
    @Json(name = "type") val type: String,
    @Json(name = "id_turn") val idTurn: Int,
    @Json(name = "finding_values") val findingValues: Map<String, Boolean>,
    @Json(name = "inventory_values") val inventoryValues: Map<String, Int>? = null,
    @Json(name = "fields_values") val fieldsValues: Map<String, String>? = null,
    @Json(name = "novelties") val novelties: List<String>? = null
)
