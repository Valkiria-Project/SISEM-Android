package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel

data class InfoCardUiModel(
    val identifier: String,
    val icon: String,
    val title: TextUiModel,
    val pill: PillUiModel,
    val date: TextUiModel?,
    val chipSection: ChipSectionUiModel?,
    val reportsDetail: ReportsDetailUiModel?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INFO_CARD
}