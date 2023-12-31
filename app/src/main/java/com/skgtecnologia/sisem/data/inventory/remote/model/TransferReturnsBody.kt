package com.skgtecnologia.sisem.data.inventory.remote.model

import com.squareup.moshi.Json

data class TransferReturnsBody(
    @Json(name = "id_turn") val idTurn: String,
    @Json(name = "fields_values") val fieldsValues: Map<String, String>,
    @Json(name = "drop_down_values") val dropDownValues: Map<String, String>,
    @Json(name = "chip_selection_values") val chipSelectionValues: Map<String, String>,
    @Json(name = "label_values") val labelValues: Map<String, String>
)
