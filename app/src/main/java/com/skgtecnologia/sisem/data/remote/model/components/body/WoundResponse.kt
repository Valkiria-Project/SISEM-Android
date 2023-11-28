@file:JvmName("HumanBodyResponseKt")

package com.skgtecnologia.sisem.data.remote.model.components.body

import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi

@JsonClass(generateAdapter = true)
data class WoundResponse(
    val type: String,
    val area: String,
    val wounds: List<String>
)

fun WoundResponse.mapToUi(): HumanBodyUi = HumanBodyUi(
    type = type,
    area = area,
    wounds = wounds
)
