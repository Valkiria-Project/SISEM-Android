package com.valkiria.uicomponents.model.ui.finding

import com.valkiria.uicomponents.components.headerbody.HeaderUiModel

data class FindingsDetailUiModel(
    val header: HeaderUiModel,
    val details: List<FindingDetailUiModel>
)
