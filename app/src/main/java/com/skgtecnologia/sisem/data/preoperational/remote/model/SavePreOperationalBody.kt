package com.skgtecnologia.sisem.data.preoperational.remote.model

import com.squareup.moshi.Json

// BACKEND: Use defaults?
data class SavePreOperationalBody(
    @Json(name = "type") val type: String,
    @Json(name = "id_turn") val idTurn: Int,
    @Json(name = "finding_values") val findingValues: Map<String, Boolean>,
    @Json(name = "inventory_values") val inventoryValues: Map<String, Int>,
    @Json(name = "extra_data") val extraData: Map<String, String>,
    @Json(name = "novelties") val novelties: List<String>
)
