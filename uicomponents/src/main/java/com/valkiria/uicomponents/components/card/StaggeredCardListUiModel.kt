package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.layout.Arrangement

data class StaggeredCardListUiModel(
    val content: List<StaggeredCardUiModel>,
    val arrangement: Arrangement.Horizontal
)
