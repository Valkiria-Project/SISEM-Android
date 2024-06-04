package com.skgtecnologia.sisem.data.medicalhistory.remote.model

import com.squareup.moshi.Json

data class DeleteAphFileBody(
    @Json(name = "id_aph") val idAph: String,
    @Json(name = "file_name") val fileName: String
)
