package com.skgtecnologia.sisem.data.medicalhistory.remote.model

import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.squareup.moshi.Json

data class HumanBodyBody(
    @Json(name = "type") val type: String,
    @Json(name = "area") val area: String,
    @Json(name = "wounds") val wounds: List<String>
)

fun HumanBodyUi.mapToBody() = HumanBodyBody(
    type = type,
    area = area,
    wounds = wounds
)