package com.valkiria.uicomponents.bricks.banner.finding

import com.valkiria.uicomponents.components.header.HeaderUiModel

data class FindingsDetailUiModel(
    val header: HeaderUiModel,
    val details: List<FindingDetailUiModel>,
    val title: String? = null
)
