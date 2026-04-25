package com.valkiria.uicomponents.components.timeline

import com.valkiria.uicomponents.components.label.TextUiModel

data class TimelineItemUiModel(
    val title: TextUiModel,
    val description: TextUiModel,
    val color: String,
    val icon: String?
)
