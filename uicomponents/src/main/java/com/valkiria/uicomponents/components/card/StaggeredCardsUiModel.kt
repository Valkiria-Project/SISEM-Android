package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class StaggeredCardsUiModel(
    override val identifier: String,
    val cards: List<StaggeredCardListUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {
    
    override val type: BodyRowType = BodyRowType.STAGGERED_CARDS
}
