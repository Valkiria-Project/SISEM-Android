package com.valkiria.uicomponents.components.card

import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel

data class CardUiModel(
    val identifier: String = "",
    val isPill: Boolean = false,
    val patient: String? = null,
    val isClickCard: Boolean = false,
    val reportDetail: ReportsDetailUiModel? = null,
    val chipSection: ChipSectionUiModel? = null
)
