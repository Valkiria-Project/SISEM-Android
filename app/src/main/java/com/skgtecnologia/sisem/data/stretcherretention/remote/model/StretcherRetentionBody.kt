package com.skgtecnologia.sisem.data.stretcherretention.remote.model

import com.squareup.moshi.Json

data class StretcherRetentionBody(
    @Json(name = "id_aph") val idAph: String,
    @Json(name = "fields_values") val fieldsValues: Map<String, String>,
    @Json(name = "chip_selection_values") val chipSelectionValues: Map<String, String>
)
