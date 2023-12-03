package com.skgtecnologia.sisem.data.remote.model.components.card

import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.card.StaggeredCardUiModel

@JsonClass(generateAdapter = true)
data class StaggeredCardResponse(
    @Json(name = "label") val label: TextResponse?,
    @Json(name = "value") val value: TextResponse?
)

fun StaggeredCardResponse.mapToUi(): StaggeredCardUiModel = StaggeredCardUiModel(
    label = label?.mapToUi() ?: error("StaggeredCard label cannot be null"),
    value = value?.mapToUi() ?: error("StaggeredCard value cannot be null")
)
