package com.skgtecnologia.sisem.data.remote.model.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.card.StaggeredCardsUiModel

@JsonClass(generateAdapter = true)
data class StaggeredCardsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "cards") val cards: List<StaggeredCardListResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.STAGGERED_CARDS

    override fun mapToUi(): StaggeredCardsUiModel = StaggeredCardsUiModel(
        identifier = identifier ?: error("StaggeredCards identifier cannot be null"),
        cards = cards?.map { it.mapToUi() } ?: error("StaggeredCards cards cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
