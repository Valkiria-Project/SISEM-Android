@file:JvmName("HumanBodyResponseKt")

package com.skgtecnologia.sisem.core.data.remote.model.components.humanbody

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi

@JsonClass(generateAdapter = true)
data class WoundResponse(
    val type: String,
    val area: String,
    @Json(name = "area_name") val areaName: String,
    val wounds: List<String>
)

fun WoundResponse.mapToUi(): HumanBodyUi = HumanBodyUi(
    type = type,
    area = area,
    areaName = areaName,
    wounds = wounds
)
