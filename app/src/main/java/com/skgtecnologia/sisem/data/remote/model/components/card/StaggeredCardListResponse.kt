package com.skgtecnologia.sisem.data.remote.model.components.card

import androidx.compose.foundation.layout.Arrangement
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.card.StaggeredCardListUiModel

@JsonClass(generateAdapter = true)
data class StaggeredCardListResponse(
    @Json(name = "content") val content: List<StaggeredCardResponse>?,
    @Json(name = "align") val arrangement: Arrangement.Horizontal?
)

fun StaggeredCardListResponse.mapToUi() = StaggeredCardListUiModel(
    content = content?.map { it.mapToUi() } ?: error("StaggeredCardList content cannot be null"),
    arrangement = arrangement ?: Arrangement.Center
)
